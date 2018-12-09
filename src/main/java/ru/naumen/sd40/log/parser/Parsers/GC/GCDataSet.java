package ru.naumen.sd40.log.parser.Parsers.GC;

import com.google.common.collect.Lists;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;

import java.util.HashMap;
import java.util.List;

public class GCDataSet implements IDataSet {
    public static final String GCTIMES = "gcTimes";
    public static final String AVARAGE_GC_TIME = "avgGcTime";
    public static final String MAX_GC_TIME = "maxGcTime";
    private final DescriptiveStatistics stat = new DescriptiveStatistics();

    @Override
    public boolean isNan() {
        return stat.getN() == 0;
    }

    @Override
    public HashMap<String, Object> getStat() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(GCTIMES, stat.getN());
        result.put(AVARAGE_GC_TIME, stat.getMean());
        result.put(MAX_GC_TIME, stat.getMax());

        return result;
    }

    @Override
    public List<String> getProps() {
        return Lists.newArrayList(GCTIMES, AVARAGE_GC_TIME, MAX_GC_TIME);
    }

    public void addValue(Double value) { stat.addValue(value); }
}
