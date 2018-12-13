package ru.naumen.sd40.log.parser.parsers.top;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.IDataParser;
import ru.naumen.sd40.log.parser.parsers.IDataSetCreator;
import ru.naumen.sd40.log.parser.parsers.ILogParser;
import ru.naumen.sd40.log.parser.parsers.ITimeParserCreator;

import javax.inject.Inject;

@Component
public class TOPLogParser implements ILogParser {
    private final TOPTimeParserCreator timeParserCreator;
    private final TOPDataSetCreator dataSetCreator;
    private final TOPDataParser dataParser;

    @Inject
    public TOPLogParser(TOPTimeParserCreator timeParserCreator, TOPDataParser dataParser, TOPDataSetCreator dataSetCreator) {
        this.timeParserCreator = timeParserCreator;
        this.dataParser = dataParser;
        this.dataSetCreator = dataSetCreator;
    }

    @Override
    public ITimeParserCreator getTimeParserCreator() {
        return timeParserCreator;
    }

    @Override
    public IDataSetCreator getDataSetCreator() {
        return dataSetCreator;
    }

    @Override
    public IDataParser getDataParser() {
        return dataParser;
    }
}
