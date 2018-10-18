package ru.naumen.sd40.log.parser.Parsers.TOP;

import ru.naumen.sd40.log.parser.DataSet;
import ru.naumen.sd40.log.parser.Parsers.IDataParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TOPDataParser implements IDataParser {
    @Override
    public void ParseLine(DataSet dataSet, String line) {
        //get la
        Matcher la = laPattern.matcher(line);
        if (la.find())
            dataSet.cpuData().addLa(Double.parseDouble(la.group(1).split(",")[0].trim()));

        //get cpu and mem
        Matcher cpuAndMemMatcher = cpuAndMemPattren.matcher(line);
        if (cpuAndMemMatcher.find())
        {
            dataSet.cpuData().addCpu(Double.valueOf(cpuAndMemMatcher.group(1)));
            dataSet.cpuData().addMem(Double.valueOf(cpuAndMemMatcher.group(2)));
        }
    }

    private static Pattern laPattern = Pattern
            .compile(".*load average:(.*)",
                    Pattern.MULTILINE);

    private static Pattern cpuAndMemPattren = Pattern
            .compile("^ *\\d+ \\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ \\S+ +(\\S+) +(\\S+) +\\S+ java",
                    Pattern.MULTILINE);
}
