package ru.naumen.sd40.log.parser.Parsers.SDNG;

import com.google.common.collect.Lists;
import ru.naumen.sd40.log.parser.Parsers.IDataType;

import java.util.List;

public class ResponseTimesDataType implements IDataType {
    public static final String PERCENTILE50 = "percent50";
    public static final String PERCENTILE95 = "percent95";
    public static final String PERCENTILE99 = "percent99";
    public static final String PERCENTILE999 = "percent999";
    public static final String MAX = "max";
    public static final String MIN = "min";
    public static final String COUNT = "count";
    public static final String ERRORS = "errors";
    public static final String MEAN = "mean";
    public static final String STDDEV = "stddev";

    @Override
    public List<String> getProps() {
        return Lists.newArrayList(COUNT, ERRORS, MEAN, STDDEV, PERCENTILE50, PERCENTILE95, PERCENTILE99, PERCENTILE999, MAX);
    }
}
