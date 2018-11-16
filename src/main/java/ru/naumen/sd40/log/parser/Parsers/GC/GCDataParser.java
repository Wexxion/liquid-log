package ru.naumen.sd40.log.parser.Parsers.GC;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.IDataParser;
import ru.naumen.sd40.log.parser.Parsers.ParserSettings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GCDataParser implements IDataParser<GCDataSet> {
    private static final Pattern gcExecutionTime = Pattern.compile(".*real=(.*)secs.*");

    @Override
    public void parseLine(GCDataSet dataSet, String line) {
        Matcher matcher = gcExecutionTime.matcher(line);
        if (matcher.find())
            dataSet.addValue(Double.parseDouble(matcher.group(1).trim().replace(',', '.')));
    }

    @Override
    public void configureViaSettings(ParserSettings settings) { }
}
