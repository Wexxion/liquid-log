package ru.naumen.sd40.log.parser.parsers.top;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.IDataParser;
import ru.naumen.sd40.log.parser.parsers.ParserSettings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TOPDataParser implements IDataParser<TopDataSet> {
    private static final Pattern laPattern = Pattern
            .compile(".*load average:(.*)",
                    Pattern.MULTILINE);

    private static final Pattern cpuAndMemPattren = Pattern
            .compile("^ *\\d+ \\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ \\S+ +(\\S+) +(\\S+) +\\S+ java",
                    Pattern.MULTILINE);

    @Override
    public void parseLine(TopDataSet dataSet, String line) {
        //get la
        Matcher la = laPattern.matcher(line);
        if (la.find())
            dataSet.addLa(Double.parseDouble(la.group(1).split(",")[0].trim()));

        //get cpu and mem
        Matcher cpuAndMemMatcher = cpuAndMemPattren.matcher(line);
        if (cpuAndMemMatcher.find())
        {
            dataSet.addCpu(Double.valueOf(cpuAndMemMatcher.group(1)));
            dataSet.addMem(Double.valueOf(cpuAndMemMatcher.group(2)));
        }
    }

    @Override
    public void configureViaSettings(ParserSettings settings) { }
}
