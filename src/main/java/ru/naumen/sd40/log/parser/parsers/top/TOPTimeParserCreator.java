package ru.naumen.sd40.log.parser.parsers.top;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.naumen.sd40.log.parser.parsers.ITimeParser;
import ru.naumen.sd40.log.parser.parsers.ITimeParserCreator;

@Component
@RequestScope
public class TOPTimeParserCreator implements ITimeParserCreator {
    private ITimeParser timeParser;

    public TOPTimeParserCreator() { timeParser = new TOPTimeParser(); }

    @Override
    public ITimeParser create() { return timeParser; }
}