package ru.naumen.sd40.log.parser.Parsers.SDNG;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.*;

import javax.inject.Inject;

@Component
public class SDNGLogParser implements ILogParser {
    private final ITimeParserCreator timeParserCreator;
    private final SDNGDataParser dataParser;

    @Inject
    public SDNGLogParser(SDNGTimeParserCreator timeParser, SDNGDataParser dataParser) {
        this.timeParserCreator = timeParser;
        this.dataParser = dataParser;
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

}
