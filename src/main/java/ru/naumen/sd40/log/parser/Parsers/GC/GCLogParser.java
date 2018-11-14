package ru.naumen.sd40.log.parser.Parsers.GC;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.IDataParser;
import ru.naumen.sd40.log.parser.Parsers.ILogParser;
import ru.naumen.sd40.log.parser.Parsers.ITimeParser;

import javax.inject.Inject;

@Component
public class GCLogParser implements ILogParser {
    private GCTimeParser timeParser;
    private GCDataParser dataParser;

    @Inject
    public GCLogParser(GCTimeParser timeParser, GCDataParser dataParser) {
        this.timeParser = timeParser;
        this.dataParser = dataParser;
    }

    @Override
    public ITimeParser getTimeParser() {
        return timeParser;
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
