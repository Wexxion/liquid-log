package ru.naumen.sd40.log.parser;

import ru.naumen.sd40.log.parser.Parsers.*;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;

public class LogParser {
    private static final int FiveMinutes = 5 * 60 * 1000;

    private final PartitionReader reader;
    private final ITimeParser timeParser;
    private final IDataParser dataParser;
    private final DataSetManager dataSetManager;

    LogParser(PartitionReader reader, ILogParser logParser, DataSetManager dataSetManager, ParserSettings settings) {
        this.reader = reader;
        this.timeParser = logParser.getTimeParserCreator().create();
        this.dataParser = logParser.getDataParser();
        this.dataSetManager = dataSetManager;

        this.timeParser.configureViaSettings(settings);
        this.dataParser.configureViaSettings(settings);
    }

    public void parseAndSave() throws ParseException {
        String part;
        long time;

        while ((part = reader.getNextPart()) != null) {
            if ((time = parseTime(part)) == 0)
                continue;

            long key = (time / FiveMinutes) * FiveMinutes;
            IDataSet dataSet = dataSetManager.getDataSet(key);
            dataParser.parseLine(dataSet, part);
        }
    }

    private long parseTime(String line) throws ParseException {
        Matcher matcher = timeParser.getTimePattern().matcher(line);

        if (matcher.find()) {
            String timeString = matcher.group(1);
            Date recDate = timeParser.getDate(timeString);
            return recDate.getTime();
        }
        return 0L;
    }
}
