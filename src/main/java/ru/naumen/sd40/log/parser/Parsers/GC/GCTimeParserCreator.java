package ru.naumen.sd40.log.parser.Parsers.GC;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.naumen.sd40.log.parser.Parsers.ITimeParser;
import ru.naumen.sd40.log.parser.Parsers.ITimeParserCreator;

@Component
@RequestScope
public class GCTimeParserCreator implements ITimeParserCreator {
    private ITimeParser timeParser;

    public GCTimeParserCreator() { timeParser = new GCTimeParser(); }

    @Override
    public ITimeParser create() { return timeParser; }
}
