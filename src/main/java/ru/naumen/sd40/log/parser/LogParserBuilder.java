package ru.naumen.sd40.log.parser;

import org.springframework.stereotype.Component;
import ru.naumen.perfhouse.influx.ILogStorage;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.Parsers.IDataSetCreator;
import ru.naumen.sd40.log.parser.Parsers.ILogParser;
import ru.naumen.sd40.log.parser.Parsers.ParserSettings;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

@Component
public class LogParserBuilder {
    private HashMap<String, ILogParser> parsers = new HashMap<>();
    private HashMap<String, IDataSetCreator> dataSetCreators = new HashMap<>();
    private ILogStorage logStorage = null;
    private String dbName = null;
    private String parseMode = null;
    private Path logFilepath = null;
    private String timeZone = "GMT";
    private boolean printTrace = true;

    @Inject
    public LogParserBuilder(InfluxDAO storage, List<ILogParser> logParsers, List<IDataSetCreator> logDataSetCreators) {
        this.logStorage = storage;

        for (ILogParser parser : logParsers) {
            parsers.put(parser.getModeName(), parser);
        }

        for (IDataSetCreator dataSetCreator : logDataSetCreators) {
            dataSetCreators.put(dataSetCreator.getModeName(), dataSetCreator);
        }
    }

    public LogParser build() throws IOException {
        ParserSettings settings = new ParserSettings(dbName, parseMode, timeZone, logFilepath, printTrace);
        IDataSetCreator dataSetCreator = dataSetCreators.getOrDefault(parseMode, null);
        ILogParser logParser = parsers.getOrDefault(parseMode, null);
        if(logParser == null || dataSetCreator == null)
            throw new IllegalArgumentException(
                    String.format("%s is unavailable! choose:[%s]",
                            parseMode,
                            String.join(", ", parsers.keySet())));

        DataSetManager dataSetManager = new DataSetManager(logStorage, dataSetCreator, dbName, printTrace);
        logParser.getTimeParser().SetTimeZone(timeZone);
        PartitionReader partitionReader = new PartitionReader(logFilepath, logParser.getTimeParser().GetTimePattern());
        return new LogParser(partitionReader, logParser, dataSetManager, settings);
    }

    public LogParserBuilder dbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public LogParserBuilder parseMode(String parseMode) {
        this.parseMode = parseMode;
        return this;
    }

    public LogParserBuilder logFilepath(Path logFilepath) {
        this.logFilepath = logFilepath;
        return this;
    }

    public LogParserBuilder timeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public LogParserBuilder printTrace(boolean printTrace) {
        this.printTrace = printTrace;
        return this;
    }
}
