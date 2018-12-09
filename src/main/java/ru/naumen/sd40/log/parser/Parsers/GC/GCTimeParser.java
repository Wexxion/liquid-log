package ru.naumen.sd40.log.parser.Parsers.GC;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.ITimeParser;
import ru.naumen.sd40.log.parser.Parsers.ParserSettings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

@Component
public class GCTimeParser implements ITimeParser {
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                    new Locale("ru", "RU"));

    private static final Pattern TIME_PATTERN =
            Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}\\+\\d{4}).*");

    @Override
    public Date getDate(String timeString) throws ParseException {
        return DATE_FORMAT.parse(timeString);
    }

    @Override
    public Pattern getTimePattern() {
        return TIME_PATTERN;
    }

    @Override
    public Pattern getPartitionPattern() {
        return TIME_PATTERN;
    }

    @Override
    public void setTimeZone(String timeZone) {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    @Override
    public void configureViaSettings(ParserSettings settings) { }
}
