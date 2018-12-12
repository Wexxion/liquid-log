package ru.naumen.sd40.log.parser.parsers.sdng;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.IDataParser;
import ru.naumen.sd40.log.parser.parsers.IDataSetCreator;
import ru.naumen.sd40.log.parser.parsers.ILogParser;
import ru.naumen.sd40.log.parser.parsers.ITimeParserCreator;

import javax.inject.Inject;

@Component
public class SDNGLogParser implements ILogParser {
    private final SDNGTimeParserCreator timeParserCreator;
    private final SDNGDataSetCreator dataSetCreator;
    private final SDNGDataParser dataParser;

    @Inject
    public SDNGLogParser(SDNGTimeParserCreator timeParser, SDNGDataParser dataParser, SDNGDataSetCreator dataSetCreator) {
        this.timeParserCreator = timeParser;
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
