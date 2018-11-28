package ru.naumen.sd40.log.parser.Parsers.GC;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.IDataParser;
import ru.naumen.sd40.log.parser.Parsers.ILogParser;
import ru.naumen.sd40.log.parser.Parsers.ITimeParser;
import ru.naumen.sd40.log.parser.Parsers.ITimeParserCreator;

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
    public IDataParser getDataParser() {
        return dataParser;
    }

    @Override
    public String getModeName() {
        return "gc";
    }
}
