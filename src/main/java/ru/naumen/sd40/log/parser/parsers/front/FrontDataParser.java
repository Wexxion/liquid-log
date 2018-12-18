package ru.naumen.sd40.log.parser.parsers.front;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.IDataParser;
import ru.naumen.sd40.log.parser.parsers.ParserSettings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FrontDataParser implements IDataParser<FrontDataSet> {
    private static final Pattern dataRegEx = Pattern.compile("render time: (\\d+)");

    @Override
    public void parseLine(FrontDataSet dataSet, String line) {
        Matcher matcher = dataRegEx.matcher(line);
        if (matcher.find())
            dataSet.addValue(Integer.parseInt(matcher.group(1).trim()));
    }

    @Override
    public void configureViaSettings(ParserSettings settings) { }
}
