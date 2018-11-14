package ru.naumen.sd40.log.parser.Parsers;

import java.nio.file.Path;

public class ParserSettings {
    public String dbName;
    public String parseMode;
    public String timeZone;
    public Path logFilepath;
    public boolean printTrace;

    public ParserSettings(String dbName, String parseMode, String timeZone, Path logFilepath, boolean printTrace) {

        this.dbName = dbName;
        this.parseMode = parseMode;
        this.timeZone = timeZone;
        this.logFilepath = logFilepath;
        this.printTrace = printTrace;
    }
}
