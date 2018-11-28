package ru.naumen.sd40.log.parser.Parsers.TOP;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.IDataParser;
import ru.naumen.sd40.log.parser.Parsers.ILogParser;
import ru.naumen.sd40.log.parser.Parsers.ITimeParser;
import ru.naumen.sd40.log.parser.Parsers.ITimeParserCreator;

import javax.inject.Inject;

@Component
public class TOPLogParser implements ILogParser {
    private final TOPTimeParserCreator timeParserCreator;
    private final TOPDataParser dataParser;

    @Inject
    public TOPLogParser(TOPTimeParserCreator timeParserCreator, TOPDataParser dataParser) {
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
        return "top";
    }
}
