package ru.naumen.sd40.log.parser.Parsers;

public interface ILogParser {
    ITimeParser getTimeParser();
    IDataParser getDataParser();
    String getModeName();
}
