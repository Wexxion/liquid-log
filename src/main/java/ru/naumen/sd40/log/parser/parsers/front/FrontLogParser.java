package ru.naumen.sd40.log.parser.parsers.front;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.IDataParser;
import ru.naumen.sd40.log.parser.parsers.IDataSetCreator;
import ru.naumen.sd40.log.parser.parsers.ILogParser;
import ru.naumen.sd40.log.parser.parsers.ITimeParserCreator;

import javax.inject.Inject;

@Component
public class FrontLogParser implements ILogParser {
    private final FrontTimeParserCreator timeParserCreator;
    private final FrontDataSetCreator dataSetCreator;
    private final FrontDataParser dataParser;

    @Inject
    public FrontLogParser(FrontTimeParserCreator timeParserCreator, FrontDataParser dataParser, FrontDataSetCreator dataSetCreator) {
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
