package ru.naumen.sd40.log.parser;

import ru.naumen.sd40.log.parser.Parsers.IDataParser;
import ru.naumen.sd40.log.parser.Parsers.ITimeParser;
import ru.naumen.sd40.log.parser.Parsers.PartitionReader;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;

public class LogParser {
    private static final int FiveMinutes = 5 * 60 * 1000;

    private final PartitionReader reader;
    private final ITimeParser timeParser;
    private final IDataParser dataParser;
    private final DataSetManager dataSetManager;

    public LogParser(PartitionReader reader, ITimeParser timeParser, IDataParser dataParser, DataSetManager dataSetManager) {

        this.reader = reader;
        this.timeParser = timeParser;
        this.dataParser = dataParser;
        this.dataSetManager = dataSetManager;
    }

    public void parseAndSave() throws ParseException {
        String part;
        long time;

        while ((part = reader.GetNextPart()) != null) {
            if ((time = parseTime(part)) == 0)
                continue;

            long key = (time / FiveMinutes) * FiveMinutes;
            DataSet dataSet = dataSetManager.getDataSet(key);
            dataParser.ParseLine(dataSet, part);
        }
    }

    private long parseTime(String line) throws ParseException {
        Matcher matcher = timeParser.GetTimePattern().matcher(line);

        if (matcher.find()) {
            String timeString = matcher.group(1);
            Date recDate = timeParser.GetDate(timeString);
            return recDate.getTime();
        }
        return 0L;
    }
}
