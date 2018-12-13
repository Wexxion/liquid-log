package ru.naumen.sd40.log.parser.parsers.sdng;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.naumen.sd40.log.parser.parsers.ITimeParser;
import ru.naumen.sd40.log.parser.parsers.ITimeParserCreator;

@Component
@RequestScope
public class SDNGTimeParserCreator implements ITimeParserCreator {
    private ITimeParser timeParser;

    public SDNGTimeParserCreator() { timeParser = new SDNGTimeParser(); }

    @Override
    public ITimeParser create() { return timeParser; }
}