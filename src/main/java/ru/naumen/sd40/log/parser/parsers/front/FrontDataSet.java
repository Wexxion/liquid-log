package ru.naumen.sd40.log.parser.parsers.front;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.parsers.IDataSet;

import java.util.HashMap;

public class FrontDataSet implements IDataSet {
    private final DescriptiveStatistics stat = new DescriptiveStatistics();

    @Override
    public boolean isNan() { return stat.getN() == 0; }

    @Override
    public HashMap<String, Object> getStat(boolean printTrace) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(FrontDataType.FRONT_TIMES, stat.getN());
        result.put(FrontDataType.MIN_RENDER_TIME, stat.getMin());
        result.put(FrontDataType.AVERAGE_RENDER_TIME, stat.getMean());
        result.put(FrontDataType.MAX_RENDER_TIME, stat.getMax());

        return result;
    }

    void addValue(Integer value) {
        stat.addValue(value);
    }
}
