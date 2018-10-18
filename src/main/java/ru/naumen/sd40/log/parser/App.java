package ru.naumen.sd40.log.parser;

import org.influxdb.dto.BatchPoints;
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
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Created by doki on 22.10.16.
 */
public class App {
    private static final String DefaultTimeZone = "GMT";
    private static int FiveMinutes = 5 * 60 * 1000;

    private static  ITimeParser timeParser;
    private static IDataParser dataParser;

    /**
     * @param args [0] - sdng.log, [1] - gc.log, [2] - top, [3] - dbName, [4] timezone
     * @throws IOException
     * @throws ParseException
     */
    public static void main(String[] args) throws IOException, ParseException {
        String influxDb = null;

        if (args.length > 1) {
            influxDb = args[1];
            influxDb = influxDb.replaceAll("-", "_");
        }

        InfluxDAO storage = null;
        if (influxDb != null) {
            storage = new InfluxDAO(System.getProperty("influx.host"), System.getProperty("influx.user"),
                    System.getProperty("influx.password"));
            storage.init();
            storage.connectToDB(influxDb);
        }
        InfluxDAO finalStorage = storage;
        String finalInfluxDb = influxDb;
        BatchPoints points = null;

        if (storage != null) {
            points = storage.startBatchPoints(influxDb);
        }

        String logFilename = args[0];
        String timeZone = args.length > 2 ? args[2] : DefaultTimeZone;

        HashMap<Long, DataSet> data = new HashMap<>();

        switch (System.getProperty("parse.mode", "")) {
            case "sdng":
                timeParser = new SDNGTimeParser();
                dataParser = new SDNGDataParser();
                break;
            case "gc":
                timeParser = new GCTimeParser();
                dataParser = new GCDataParser();
                break;
            case "top":
                timeParser = new TOPTimeParser(logFilename);
                dataParser = new TOPDataParser();
                break;
            default:
                throw new IllegalArgumentException("Unknown parse mode! Availiable modes: [sdng, gc, top]");
        }

        timeParser.SetTimeZone(timeZone);
        PartitionReader partitionReader = new PartitionReader(logFilename, timeParser.GetTimePattern());
        ParseLogs(data, partitionReader);

        SaveToDB(storage, finalStorage, finalInfluxDb, points, data);
    }

    private static void ParseLogs(HashMap<Long, DataSet> data, PartitionReader partitionReader) throws ParseException {
        String part;
        long time;

        while ((part = partitionReader.GetNextPart()) != null){
            if ((time = ParseTime(part)) == 0)
                continue;

            long key = (time / FiveMinutes) * FiveMinutes;
            dataParser.ParseLine(data.computeIfAbsent(key, k -> new DataSet()), part);
        }
    }

    private static long ParseTime(String line) throws ParseException{
        Matcher matcher = timeParser.GetTimePattern().matcher(line);

        if (matcher.find()) {
            String timeString = matcher.group(1);
            Date recDate = timeParser.GetDate(timeString);
            return recDate.getTime();
        }
        return 0L;
    }

    private static void SaveToDB(InfluxDAO storage, InfluxDAO finalStorage, String finalInfluxDb, BatchPoints points, HashMap<Long, DataSet> data) {
        if (System.getProperty("NoCsv") == null) {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
        }
        data.forEach((k, set) ->
        {
            ActionDoneParser dones = set.getActionsDone();
            dones.calculate();
            ErrorParser erros = set.getErrors();
            if (System.getProperty("NoCsv") == null) {
                System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", k, dones.getCount(),
                        dones.getMin(), dones.getMean(), dones.getStddev(), dones.getPercent50(), dones.getPercent95(),
                        dones.getPercent99(), dones.getPercent999(), dones.getMax(), erros.getErrorCount()));
            }
            if (!dones.isNan()) {
                finalStorage.storeActionsFromLog(points, finalInfluxDb, k, dones, erros);
            }

            GCParser gc = set.getGc();
            if (!gc.isNan()) {
                finalStorage.storeGc(points, finalInfluxDb, k, gc);
            }

            TopData cpuData = set.cpuData();
            if (!cpuData.isNan()) {
                finalStorage.storeTop(points, finalInfluxDb, k, cpuData);
            }
        });
        storage.writeBatch(points);
    }
}
