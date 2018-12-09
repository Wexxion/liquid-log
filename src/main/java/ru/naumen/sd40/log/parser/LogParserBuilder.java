package ru.naumen.sd40.log.parser;

import org.springframework.stereotype.Component;
import ru.naumen.perfhouse.influx.ILogStorage;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.Parsers.ILogParser;
import ru.naumen.sd40.log.parser.Parsers.ITimeParser;
import ru.naumen.sd40.log.parser.Parsers.ParserSettings;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Component
public class LogParserBuilder {
    private final ILogStorage logStorage;
    private final Map<String, ILogParser> parsers;

    @Inject
    public LogParserBuilder(InfluxDAO storage, Map<String, ILogParser> parsers) {
        this.logStorage = storage;
        this.parsers = parsers;
    }

    public Set<String> getAvailableParseModes() {
        return parsers.keySet();
    }

    public LogParser build(ParserSettings settings) throws IOException {
        ILogParser logParser = parsers.getOrDefault(settings.parseMode, null);
        if(logParser == null)
            throw new IllegalArgumentException(
                    String.format("%s is unavailable! choose:[%s]",
                            settings.parseMode,
                            String.join(", ", parsers.keySet())));

        DataSetManager dataSetManager = new DataSetManager(logStorage, logParser.getDataSetCreator(), settings.dbName, settings.printTrace);
        ITimeParser timeParser = logParser.getTimeParserCreator().create();
        timeParser.setTimeZone(settings.timeZone);
        PartitionReader partitionReader = new PartitionReader(settings.logFilepath, timeParser.getPartitionPattern());
        return new LogParser(partitionReader, logParser, dataSetManager, settings);
    }
}
