package ru.naumen.sd40.log.parser.parsers.gc;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.IDataParser;
import ru.naumen.sd40.log.parser.parsers.IDataSetCreator;
import ru.naumen.sd40.log.parser.parsers.ILogParser;
import ru.naumen.sd40.log.parser.parsers.ITimeParserCreator;

import javax.inject.Inject;

@Component
public class GCLogParser implements ILogParser {
    private final GCTimeParserCreator timeParserCreator;
    private final GCDataSetCreator dataSetCreator;
    private final GCDataParser dataParser;

    @Inject
    public GCLogParser(GCTimeParserCreator timeParserCreator, GCDataParser dataParser, GCDataSetCreator dataSetCreator) {
        this.timeParserCreator = timeParserCreator;
        this.dataParser = dataParser;
        this.dataSetCreator = dataSetCreator;
    }

    @Override
    public ITimeParserCreator getTimeParserCreator() {
        return timeParserCreator;
    }

    @Override
    public IDataSetCreator getDataSetCreator() {
        return dataSetCreator;
    }

    @Override
    public IDataParser getDataParser() {
        return dataParser;
    }
}
