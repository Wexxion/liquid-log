package ru.naumen.sd40.log.parser.Parsers.GC;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;

public class GCDataSet implements IDataSet {
    private DescriptiveStatistics stat = new DescriptiveStatistics();

    @Override
    public boolean isNan() {
        return stat.getN() == 0;
    }

    @Override
    public IDataSet create() {
        return new GCDataSet();
    }

    public DescriptiveStatistics getFinalStat() { return stat.copy(); }
    public void addValue(Double value) {stat.addValue(value);}
}
