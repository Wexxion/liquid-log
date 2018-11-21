package ru.naumen.sd40.log.parser.Parsers.SDNG;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.IDataParser;
import ru.naumen.sd40.log.parser.Parsers.ILogParser;
import ru.naumen.sd40.log.parser.Parsers.ITimeParser;

import javax.inject.Inject;

@Component
public class SDNGLogParser implements ILogParser {
    private final SDNGTimeParser timeParser;
    private final SDNGDataParser dataParser;

    @Inject
    public SDNGLogParser(SDNGTimeParser timeParser, SDNGDataParser dataParser) {
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
        return "sdng";
    }
}
