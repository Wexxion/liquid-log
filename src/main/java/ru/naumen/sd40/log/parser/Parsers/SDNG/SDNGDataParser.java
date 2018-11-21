package ru.naumen.sd40.log.parser.Parsers.SDNG;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.IDataParser;
import ru.naumen.sd40.log.parser.Parsers.ParserSettings;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SDNGDataParser implements IDataParser<SDNGDataSet> {
    private static final Pattern warnRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) WARN");
    private static final Pattern errorRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) ERROR");
    private static final Pattern fatalRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) FATAL");
    private static final Pattern doneRegEx = Pattern.compile("Done\\((\\d+)\\): ?(.*?Action)");
    private static final Set<String> EXCLUDED_ACTIONS =
            new HashSet<>(Stream.of(
                    SDNGDataSet.ActionType.EventAction
            ).map(Enum::name).collect(Collectors.toList()));

    @Override
    public void configureViaSettings(ParserSettings settings) {
        if(settings.printTrace){
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
        }
    }

    @Override
    public void parseLine(SDNGDataSet dataSet, String line) {
        parseErrors(dataSet, line);
        parseActions(dataSet, line);
    }

    private void parseErrors(SDNGDataSet dataSet, String line) {
        if (warnRegEx.matcher(line).find())
            dataSet.incrementErrorCounter(SDNGDataSet.ErrorType.Warn);
        if (errorRegEx.matcher(line).find())
            dataSet.incrementErrorCounter(SDNGDataSet.ErrorType.Error);
        if (fatalRegEx.matcher(line).find())
            dataSet.incrementErrorCounter(SDNGDataSet.ErrorType.Fatal);
    }

    private void parseActions(SDNGDataSet dataSet, String line) {
        Matcher matcher = doneRegEx.matcher(line);

        if (matcher.find()) {

            String actionInLowerCase = matcher.group(2).toLowerCase();
            if (EXCLUDED_ACTIONS.contains(actionInLowerCase))
                return;

            dataSet.addTimes(Integer.parseInt(matcher.group(1)));

            if (actionInLowerCase.equals("addobjectaction"))
                dataSet.incrementActionCounter(SDNGDataSet.ActionType.AddObjectAction);
            else if (actionInLowerCase.equals("editobjectaction"))
                dataSet.incrementActionCounter(SDNGDataSet.ActionType.EditObjectsAction);
            else if (actionInLowerCase.equals("getcatalogsaction"))
                dataSet.incrementActionCounter(SDNGDataSet.ActionType.GetCatalogsAction);
            else if (actionInLowerCase.matches("(?i)[a-zA-Z]+comment[a-zA-Z]+"))
                dataSet.incrementActionCounter(SDNGDataSet.ActionType.CommentAction);
            else if (!actionInLowerCase.contains("advlist")
                    && actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+List[a-zA-Z]+"))
                dataSet.incrementActionCounter(SDNGDataSet.ActionType.GetListAction);
            else if (actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+Form[a-zA-Z]+"))
                dataSet.incrementActionCounter(SDNGDataSet.ActionType.GetFormAction);
            else if (actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+DtObject[a-zA-Z]+"))
                dataSet.incrementActionCounter(SDNGDataSet.ActionType.GetDtObjectAction);
            else if (actionInLowerCase.matches("(?i)[a-zA-Z]+search[a-zA-Z]+"))
                dataSet.incrementActionCounter(SDNGDataSet.ActionType.SearchAction);
        }
    }
}
