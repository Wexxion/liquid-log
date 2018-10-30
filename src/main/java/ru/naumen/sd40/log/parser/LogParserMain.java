package ru.naumen.sd40.log.parser;

import ru.naumen.perfhouse.influx.ILogStorage;
import ru.naumen.sd40.log.parser.Parsers.GC.GCDataParser;
import ru.naumen.sd40.log.parser.Parsers.GC.GCTimeParser;
import ru.naumen.sd40.log.parser.Parsers.IDataParser;
import ru.naumen.sd40.log.parser.Parsers.ITimeParser;
import ru.naumen.sd40.log.parser.Parsers.PartitionReader;
import ru.naumen.sd40.log.parser.Parsers.SDNG.SDNGDataParser;
import ru.naumen.sd40.log.parser.Parsers.SDNG.SDNGTimeParser;
import ru.naumen.sd40.log.parser.Parsers.TOP.TOPDataParser;
import ru.naumen.sd40.log.parser.Parsers.TOP.TOPTimeParser;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;

public final class LogParserMain {
    public static void Parse(ILogStorage storage, String dbName, String parseMode,
                             Path logFilepath, String timeZone, boolean noCsv) throws IOException, ParseException {
        DataSetManager dataSetManager = new DataSetManager(storage, dbName, noCsv);
        ITimeParser timeParser = getTimeParser(logFilepath.toString(), parseMode);
        IDataParser dataParser = getDataParser(parseMode);

        timeParser.SetTimeZone(timeZone);
        PartitionReader partitionReader = new PartitionReader(logFilepath, timeParser.GetTimePattern());

        new LogParser(partitionReader, timeParser, dataParser, dataSetManager).parseAndSave();
    }

    private static IDataParser getDataParser(String parseMode) {
        switch (parseMode) {
            case "sdng":
                return new SDNGDataParser();
            case "gc":
                return new GCDataParser();
            case "top":
                return new TOPDataParser();
            default:
                throw new IllegalArgumentException("Unknown parse mode! Availiable modes: [sdng, gc, top]");
        }
    }

    private static ITimeParser getTimeParser(String logFilename, String parseMode) {
        switch (parseMode) {
            case "sdng":
                return new SDNGTimeParser();
            case "gc":
                return new GCTimeParser();
            case "top":
                return new TOPTimeParser(logFilename);
            default:
                throw new IllegalArgumentException("Unknown parse mode! Availiable modes: [sdng, gc, top]");
        }
    }
}
