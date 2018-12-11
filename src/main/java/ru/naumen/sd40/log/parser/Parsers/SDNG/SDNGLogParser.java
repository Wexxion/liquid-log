package ru.naumen.sd40.log.parser.Parsers.SDNG;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.*;

import javax.inject.Inject;
import java.util.List;

@Component
public class SDNGLogParser implements ILogParser {
    private final ITimeParserCreator timeParserCreator;
    private final SDNGDataParser dataParser;
    private final PerformedActionsDataType performedActionsDataType;
    private final ResponseTimesDataType responseTimesDataType;

    @Inject
    public SDNGLogParser(SDNGTimeParserCreator timeParser, SDNGDataParser dataParser) {
        this.timeParserCreator = timeParser;
        this.dataParser = dataParser;
        this.performedActionsDataType = new PerformedActionsDataType();
        this.responseTimesDataType = new ResponseTimesDataType();
    }

    @Override
    public ITimeParserCreator getTimeParserCreator() {
        return timeParserCreator;
    }

    @Override
    public IDataSetCreator getDataSetCreator() {
        return new SDNGDataSetCreator();
    }

    @Override
    public IDataParser getDataParser() {
        return dataParser;
    }

    @Override
    public List<IDataType> getDataTypes() {
        return Lists.newArrayList(performedActionsDataType, responseTimesDataType);
    }

}
