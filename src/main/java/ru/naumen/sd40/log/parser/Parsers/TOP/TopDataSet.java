package ru.naumen.sd40.log.parser.Parsers.TOP;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;

import java.util.HashMap;

import static ru.naumen.sd40.log.parser.NumberUtils.prepareDouble;

public class TopDataSet implements IDataSet {
    private final DescriptiveStatistics laStat = new DescriptiveStatistics();
    private final DescriptiveStatistics cpuStat = new DescriptiveStatistics();
    private final DescriptiveStatistics memStat = new DescriptiveStatistics();

    @Override
    public boolean isNan() {
        return laStat.getN() == 0 && cpuStat.getN() == 0 && memStat.getN() == 0;
    }

    @Override
    public HashMap<String, Object> getStat(boolean printTrace) {
        HashMap<String, Object> result = new HashMap<>();
        result.put(TopDataType.AVG_LA, prepareDouble(laStat.getMean()));
        result.put(TopDataType.AVG_CPU, prepareDouble(cpuStat.getMean()));
        result.put(TopDataType.AVG_MEM, prepareDouble(memStat.getMean()));
        result.put(TopDataType.MAX_LA, prepareDouble(laStat.getMax()));
        result.put(TopDataType.MAX_CPU, prepareDouble(cpuStat.getMax()));
        result.put(TopDataType.MAX_MEM, prepareDouble(memStat.getMax()));
        return result;
    }

    void addLa(double la) {
        laStat.addValue(la);
    }

    void addCpu(double cpu) {
        cpuStat.addValue(cpu);
    }

    void addMem(double mem) {
        memStat.addValue(mem);
    }
}
