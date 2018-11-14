package ru.naumen.sd40.log.parser.Parsers.SDNG;

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
public class SDNGTimeParser implements ITimeParser {
    @Override
    public Date GetDate(String timeString) throws ParseException {
        return DATE_FORMAT.parse(timeString);
    }

    @Override
    public void configureViaSettings(ParserSettings settings) { }

    @Override
    public Pattern GetTimePattern() {
        return TIME_PATTERN;
    }

    @Override
    public void SetTimeZone(String timeZone) {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("dd MMM yyyy HH:mm:ss,SSS",
                    new Locale("ru", "RU"));

    private static final Pattern TIME_PATTERN =
            Pattern.compile("^\\d+ \\[.*?\\] \\((\\d{2} .{3} \\d{4} \\d{2}:\\d{2}:\\d{2},\\d{3})\\)");
}
