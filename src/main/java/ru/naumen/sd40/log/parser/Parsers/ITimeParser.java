package ru.naumen.sd40.log.parser.Parsers;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

public interface ITimeParser {
    Date GetDate(String timeString) throws ParseException;
    Pattern GetTimePattern();
    void SetTimeZone(String timeZone);
    void configureViaSettings(ParserSettings settings);
}
