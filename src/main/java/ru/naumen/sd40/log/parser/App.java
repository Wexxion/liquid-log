package ru.naumen.sd40.log.parser;

import com.sun.javaws.exceptions.InvalidArgumentException;
import ru.naumen.perfhouse.influx.ILogStorage;
import ru.naumen.perfhouse.influx.InfluxDAO;
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
import java.text.ParseException;

/**
 * Created by doki on 22.10.16.
 */
public class App {
    private static final String DefaultTimeZone = "GMT";

    /**
     * @param args [0] - sdng.log, [1] - gc.log, [2] - top, [3] - dbName, [4] timezone
     * @throws IOException
     * @throws ParseException
     * @throws InvalidArgumentException
     */
    public static void main(String[] args) throws InvalidArgumentException, IOException, ParseException {
        if (args.length < 1)
            throw new InvalidArgumentException(args);

        String logFilename = args[0];
        DataSetManager dataSetManager = getDataSetManager(args[1]);
        String timeZone = args.length > 2 ? args[2] : DefaultTimeZone;
        String parseMode = System.getProperty("parse.mode", "");

        ITimeParser timeParser = getTimeParser(logFilename, parseMode);
        IDataParser dataParser = getDataParser(parseMode);

        timeParser.SetTimeZone(timeZone);
        PartitionReader partitionReader = new PartitionReader(logFilename, timeParser.GetTimePattern());

        new LogParser(partitionReader, timeParser, dataParser, dataSetManager).parseAndSave();
    }

    private static DataSetManager getDataSetManager(String dbNameArg) {
        String dbName = dbNameArg.replaceAll("-", "_");
        ILogStorage influx = new InfluxDAO(System.getProperty("influx.host"),
                System.getProperty("influx.user"),
                System.getProperty("influx.password"));
        boolean noCsv = System.getProperty("NoCsv") != null;
        return new DataSetManager(influx, dbName, noCsv);
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
