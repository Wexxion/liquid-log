package ru.naumen.sd40.log.parser.Parsers;

public interface ILogParser {
    ITimeParserCreator getTimeParserCreator();
    IDataSetCreator getDataSetCreator();
    IDataParser getDataParser();
}
