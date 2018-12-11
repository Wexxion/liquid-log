package ru.naumen.sd40.log.parser.Parsers;

import java.util.List;

public interface ILogParser {
    ITimeParserCreator getTimeParserCreator();
    IDataSetCreator getDataSetCreator();
    IDataParser getDataParser();
    List<IDataType> getDataTypes();
}
