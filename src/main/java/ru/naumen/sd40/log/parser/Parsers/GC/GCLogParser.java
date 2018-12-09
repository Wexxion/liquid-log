package ru.naumen.sd40.log.parser.Parsers.GC;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.*;

import javax.inject.Inject;

@Component
public class GCLogParser implements ILogParser {
    private final GCTimeParserCreator timeParserCreator;
    private final GCDataParser dataParser;

    @Inject
    public GCLogParser(GCTimeParserCreator timeParserCreator, GCDataParser dataParser) {
        this.timeParserCreator = timeParserCreator;
        this.dataParser = dataParser;
    }

    @Override
    public ITimeParserCreator getTimeParserCreator() {
        return timeParserCreator;
    }

    @Override
    public IDataSetCreator getDataSetCreator() {
        return new GCDataSetCreator();
    }

    @Override
    public IDataParser getDataParser() {
        return dataParser;
    }
}
