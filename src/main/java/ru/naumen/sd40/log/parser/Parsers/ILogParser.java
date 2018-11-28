package ru.naumen.sd40.log.parser.Parsers;

public interface ILogParser {
    ITimeParserCreator getTimeParserCreator();
    IDataParser getDataParser();
    String getModeName();
}
