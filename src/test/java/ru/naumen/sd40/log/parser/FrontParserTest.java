package ru.naumen.sd40.log.parser;

import org.junit.Assert;
import org.junit.Test;
import ru.naumen.sd40.log.parser.parsers.IDataParser;
import ru.naumen.sd40.log.parser.parsers.ITimeParser;
import ru.naumen.sd40.log.parser.parsers.front.FrontDataParser;
import ru.naumen.sd40.log.parser.parsers.front.FrontDataSet;
import ru.naumen.sd40.log.parser.parsers.front.FrontDataType;
import ru.naumen.sd40.log.parser.parsers.front.FrontTimeParser;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;

public class FrontParserTest {
    private static final String sample = "102725313 2017-12-13 03:48:56,048 [http-nio-8443-exec-83 operator1 fs000080000m0jaoh10o2ito00] DEBUG AdvFormEngine - session: fs000080000m0jaoh10o2ito00 render time: 18";

    @Test
    public void mustParseTimeFromSample() throws ParseException {
        //given
        ITimeParser timeParser = new FrontTimeParser();

        //then and when
        Matcher matcher = timeParser.getTimePattern().matcher(sample);
        Assert.assertTrue(matcher.find());

        String timeString = matcher.group(1);
        Assert.assertEquals("2017-12-13 03:48:56,048", timeString);

        Date recDate = timeParser.getDate(timeString);
        Assert.assertEquals(1513118936048L, recDate.getTime());
    }

    @Test
    public void mustParseDataFromSample() {
        //given
        FrontDataSet dataSet = new FrontDataSet();
        IDataParser parser = new FrontDataParser();

        //when
        parser.parseLine(dataSet, sample);

        //then
        Assert.assertEquals(1L, (dataSet.getStat(false).get(FrontDataType.FRONT_TIMES)));
        Assert.assertEquals(18.0, (dataSet.getStat(false).get(FrontDataType.MAX_RENDER_TIME)));
    }
}
