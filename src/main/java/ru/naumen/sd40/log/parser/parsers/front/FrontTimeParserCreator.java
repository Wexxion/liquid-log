package ru.naumen.sd40.log.parser.parsers.front;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.naumen.sd40.log.parser.parsers.ITimeParser;
import ru.naumen.sd40.log.parser.parsers.ITimeParserCreator;

@Component
@RequestScope
public class FrontTimeParserCreator implements ITimeParserCreator {
    private ITimeParser timeParser;

    public FrontTimeParserCreator() { timeParser = new FrontTimeParser(); }

    @Override
    public ITimeParser create() { return timeParser; }
}
