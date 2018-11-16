package ru.naumen.sd40.log.parser.Parsers.TOP;

import org.springframework.context.annotation.Scope;
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
@Scope("prototype")
public class TOPTimeParser implements ITimeParser {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHH:mm");
    private static final Pattern TIME_PATTERN = Pattern.compile("^_+ (\\S+)");

    private String dataDate;

    @Override
    public void configureViaSettings(ParserSettings settings) {
        Matcher matcher = Pattern.compile("\\d{8}|\\d{4}-\\d{2}-\\d{2}")
                .matcher(settings.logFilepath.getFileName().toString());
        if (!matcher.find()) throw new IllegalArgumentException();
        this.dataDate = matcher.group(0).replaceAll("-", "");
    }

    @Override
    public Date getDate(String timeString) throws ParseException {
        return DATE_FORMAT.parse(dataDate + timeString);
    }

    @Override
    public Pattern getTimePattern() {
        return TIME_PATTERN;
    }

    @Override
    public void setTimeZone(String timeZone) {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
}
