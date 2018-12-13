package ru.naumen.sd40.log.parser.parsers;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

public interface ITimeParser {
    Date getDate(String timeString) throws ParseException;
    Pattern getTimePattern();
    Pattern getPartitionPattern();
    void setTimeZone(String timeZone);
    void configureViaSettings(ParserSettings settings);
}
