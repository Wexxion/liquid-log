package ru.naumen.sd40.log.parser.Parsers.GC;

import com.google.common.collect.Lists;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.perfhouse.statdata.Constants;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;

import java.util.HashMap;
import java.util.List;

public class GCDataSet implements IDataSet {
    private final DescriptiveStatistics stat = new DescriptiveStatistics();

    @Override
    public boolean isNan() {
        return stat.getN() == 0;
    }

    @Override
    public HashMap<String, Object> getStat(boolean printTrace) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(Fields.GCTIMES, stat.getN());
        result.put(Fields.AVARAGE_GC_TIME, stat.getMean());
        result.put(Fields.MAX_GC_TIME, stat.getMax());

        return result;
    }

    void addValue(Double value) {
        stat.addValue(value);
    }

    public static class Fields {
        public static final String GCTIMES = "gcTimes";
        public static final String AVARAGE_GC_TIME = "avgGcTime";
        public static final String MAX_GC_TIME = "maxGcTime";

        public static List<String> getProps()
        {
            return Lists.newArrayList(Constants.TIME, GCTIMES, AVARAGE_GC_TIME, MAX_GC_TIME);
        }
    }
}
