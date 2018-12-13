package ru.naumen.sd40.log.parser.parsers;

public interface ILogParser {
    ITimeParserCreator getTimeParserCreator();
    IDataSetCreator getDataSetCreator();
    IDataParser getDataParser();
}
