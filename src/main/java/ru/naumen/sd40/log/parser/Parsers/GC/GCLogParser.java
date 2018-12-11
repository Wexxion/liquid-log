package ru.naumen.sd40.log.parser.Parsers.GC;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.*;

import javax.inject.Inject;
import java.util.List;

@Component
public class GCLogParser implements ILogParser {
    private final GCTimeParserCreator timeParserCreator;
    private final GCDataParser dataParser;
    private final GCDataType dataType;

    @Inject
    public GCLogParser(GCTimeParserCreator timeParserCreator, GCDataParser dataParser) {
        this.timeParserCreator = timeParserCreator;
        this.dataParser = dataParser;
        this.dataType = new GCDataType();
    }

    @Override
    public ITimeParserCreator getTimeParserCreator() {
        return timeParserCreator;
    }

    @Override
    public IDataSetCreator getDataSetCreator() {
        return new GCDataSetCreator();
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
