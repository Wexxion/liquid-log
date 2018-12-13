package ru.naumen.sd40.log.parser.parsers.gc;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.naumen.sd40.log.parser.parsers.ITimeParser;
import ru.naumen.sd40.log.parser.parsers.ITimeParserCreator;

@Component
@RequestScope
public class GCTimeParserCreator implements ITimeParserCreator {
    private ITimeParser timeParser;

    public GCTimeParserCreator() { timeParser = new GCTimeParser(); }

    @Override
    public ITimeParser create() { return timeParser; }
}
