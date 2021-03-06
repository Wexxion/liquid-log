package ru.naumen.sd40.log.parser.parsers.front;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.ITimeParser;
import ru.naumen.sd40.log.parser.parsers.ParserSettings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

@Component
public class FrontTimeParser implements ITimeParser {
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS",
                    new Locale("ru", "RU"));

    private static final Pattern TIME_PATTERN =
            Pattern.compile("^\\d+ (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})");

    @Override
    public Date getDate(String timeString) throws ParseException { return DATE_FORMAT.parse(timeString); }

    @Override
    public Pattern getTimePattern()  {
        return TIME_PATTERN;
    }

    @Override
    public Pattern getPartitionPattern()  {
        return TIME_PATTERN;
    }

    @Override
    public void setTimeZone(String timeZone)  {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    @Override
    public void configureViaSettings(ParserSettings settings) { }
}
