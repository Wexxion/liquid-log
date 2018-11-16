package ru.naumen.sd40.log.parser.Parsers.TOP;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;

public class TopDataSet implements IDataSet {
    private final DescriptiveStatistics laStat = new DescriptiveStatistics();
    private final DescriptiveStatistics cpuStat = new DescriptiveStatistics();
    private final DescriptiveStatistics memStat = new DescriptiveStatistics();

    @Override
    public boolean isNan() {
        return laStat.getN() == 0 && cpuStat.getN() == 0 && memStat.getN() == 0;
    }

    public void addLa(double la) {
        laStat.addValue(la);
    }

    public void addCpu(double cpu) {
        cpuStat.addValue(cpu);
    }

    public void addMem(double mem) {
        memStat.addValue(mem);
    }

    public DescriptiveStatistics getFinalLaStat() {
        return laStat.copy();
    }

    public DescriptiveStatistics getFinalCpuStat() {
        return cpuStat.copy();
    }

    public DescriptiveStatistics getFinalMemStat() {
        return memStat.copy();
    }
}
