package ru.naumen.sd40.log.parser.Parsers.TOP;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.*;

import javax.inject.Inject;
import java.util.List;

@Component
public class TOPLogParser implements ILogParser {
    private final TOPTimeParserCreator timeParserCreator;
    private final TOPDataParser dataParser;
    private final TopDataType dataType;

    @Inject
    public TOPLogParser(TOPTimeParserCreator timeParserCreator, TOPDataParser dataParser) {
        this.timeParserCreator = timeParserCreator;
        this.dataParser = dataParser;
        this.dataType = new TopDataType();
    }

    @Override
    public ITimeParserCreator getTimeParserCreator() {
        return timeParserCreator;
    }

    @Override
    public IDataSetCreator getDataSetCreator() {
        return new TOPDataSetCreator();
    }

    @Override
    public IDataParser getDataParser() {
        return dataParser;
    }

    @Override
    public List<IDataType> getDataTypes() {
        return Lists.newArrayList(dataType);
    }
}
