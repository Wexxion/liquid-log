package ru.naumen.sd40.log.parser.Parsers.TOP;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.IDataParser;
import ru.naumen.sd40.log.parser.Parsers.ILogParser;
import ru.naumen.sd40.log.parser.Parsers.ITimeParser;

import javax.inject.Inject;

@Component
public class TOPLogParser implements ILogParser {
    private TOPTimeParser timeParser;
    private TOPDataParser dataParser;

    @Inject
    public TOPLogParser(TOPTimeParser timeParser, TOPDataParser dataParser) {
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
        return "top";
    }
}
