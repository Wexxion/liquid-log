package ru.naumen.sd40.log.parser.Parsers;

import java.nio.file.Path;

public class ParserSettings {
    public final String dbName;
    public final String parseMode;
    public final String timeZone;
    public final Path logFilepath;
    public final boolean printTrace;

    public ParserSettings(String dbName, String parseMode, String timeZone, Path logFilepath, boolean printTrace) {

        this.dbName = dbName;
        this.parseMode = parseMode;
        this.timeZone = timeZone;
        this.logFilepath = logFilepath;
        this.printTrace = printTrace;
    }
}
