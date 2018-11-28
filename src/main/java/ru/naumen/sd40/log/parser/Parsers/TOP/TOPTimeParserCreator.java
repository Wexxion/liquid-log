package ru.naumen.sd40.log.parser.Parsers.TOP;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.naumen.sd40.log.parser.Parsers.ITimeParser;
import ru.naumen.sd40.log.parser.Parsers.ITimeParserCreator;

@Component
@RequestScope
public class TOPTimeParserCreator implements ITimeParserCreator {
    private ITimeParser timeParser;

    public TOPTimeParserCreator() { timeParser = new TOPTimeParser(); }

    @Override
    public ITimeParser create() { return timeParser; }
}