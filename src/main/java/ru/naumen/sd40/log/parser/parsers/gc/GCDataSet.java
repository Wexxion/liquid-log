package ru.naumen.sd40.log.parser.parsers.gc;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.parsers.IDataSet;

import java.util.HashMap;

public class GCDataSet implements IDataSet {
    private final DescriptiveStatistics stat = new DescriptiveStatistics();

    @Override
    public boolean isNan() {
        return stat.getN() == 0;
    }

    @Override
    public HashMap<String, Object> getStat(boolean printTrace) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(GCDataType.GCTIMES, stat.getN());
        result.put(GCDataType.AVARAGE_GC_TIME, stat.getMean());
        result.put(GCDataType.MAX_GC_TIME, stat.getMax());

        return result;
    }

    void addValue(Double value) {
        stat.addValue(value);
    }
}
