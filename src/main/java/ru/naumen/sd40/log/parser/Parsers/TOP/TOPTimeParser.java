package ru.naumen.sd40.log.parser.Parsers.TOP;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.ITimeParser;
import ru.naumen.sd40.log.parser.Parsers.ParserSettings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TOPTimeParser implements ITimeParser {
    @Override
    public Date GetDate(String timeString) throws ParseException {
        return DATE_FORMAT.parse(dataDate + timeString);
    }

    @Override
    public void configureViaSettings(ParserSettings settings) {
        Matcher matcher = Pattern.compile("\\d{8}|\\d{4}-\\d{2}-\\d{2}")
                .matcher(settings.logFilepath.getFileName().toString());
        if (!matcher.find()) throw new IllegalArgumentException();
        this.dataDate = matcher.group(0).replaceAll("-", "");
    }

    @Override
    public Pattern GetTimePattern() {
        return TIME_PATTERN;
    }

    @Override
    public void SetTimeZone(String timeZone) {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHH:mm");
    private Pattern TIME_PATTERN = Pattern.compile("^_+ (\\S+)");

    private String dataDate;
}
