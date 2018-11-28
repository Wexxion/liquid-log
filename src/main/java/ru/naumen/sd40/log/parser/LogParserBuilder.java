package ru.naumen.sd40.log.parser;

import org.springframework.stereotype.Component;
import ru.naumen.perfhouse.influx.ILogStorage;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.Parsers.IDataSetCreator;
import ru.naumen.sd40.log.parser.Parsers.ILogParser;
import ru.naumen.sd40.log.parser.Parsers.ITimeParser;
import ru.naumen.sd40.log.parser.Parsers.ParserSettings;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
public class LogParserBuilder {
    private final ILogStorage logStorage;
    private final HashMap<String, ILogParser> parsers = new HashMap<>();
    private final HashMap<String, IDataSetCreator> dataSetCreators = new HashMap<>();

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

    public LogParser build(ParserSettings settings) throws IOException {

        IDataSetCreator dataSetCreator = dataSetCreators.getOrDefault(settings.parseMode, null);
        ILogParser logParser = parsers.getOrDefault(settings.parseMode, null);
        if(logParser == null || dataSetCreator == null)
            throw new IllegalArgumentException(
                    String.format("%s is unavailable! choose:[%s]",
                            settings.parseMode,
                            String.join(", ", parsers.keySet())));

        DataSetManager dataSetManager = new DataSetManager(logStorage, dataSetCreator, settings.dbName, settings.printTrace);
        ITimeParser timeParser = logParser.getTimeParserCreator().create();
        timeParser.setTimeZone(settings.timeZone);
        PartitionReader partitionReader = new PartitionReader(settings.logFilepath, timeParser.getTimePattern());
        return new LogParser(partitionReader, logParser, dataSetManager, settings);
    }
}
