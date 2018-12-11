package ru.naumen.sd40.log.parser.Parsers.GC;

import com.google.common.collect.Lists;
import ru.naumen.sd40.log.parser.Parsers.IDataType;

import java.util.List;

public class GCDataType implements IDataType {
    public static final String GCTIMES = "gcTimes";
    public static final String AVARAGE_GC_TIME = "avgGcTime";
    public static final String MAX_GC_TIME = "maxGcTime";

    @Override
    public List<String> getProps() {
        return Lists.newArrayList(GCTIMES, AVARAGE_GC_TIME, MAX_GC_TIME);
    }
}
