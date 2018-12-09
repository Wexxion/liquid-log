package ru.naumen.sd40.log.parser.Parsers.TOP;

import com.google.common.collect.Lists;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.perfhouse.statdata.Constants;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;

import java.util.HashMap;
import java.util.List;

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
        result.put(Fields.AVG_LA, prepareDouble(laStat.getMean()));
        result.put(Fields.AVG_CPU, prepareDouble(cpuStat.getMean()));
        result.put(Fields.AVG_MEM, prepareDouble(memStat.getMean()));
        result.put(Fields.MAX_LA, prepareDouble(laStat.getMax()));
        result.put(Fields.MAX_CPU, prepareDouble(cpuStat.getMax()));
        result.put(Fields.MAX_MEM, prepareDouble(memStat.getMax()));
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

    public static class Fields {
        public static final String AVG_LA = "avgLa";
        public static final String AVG_CPU = "avgCpu";
        public static final String AVG_MEM = "avgMem";
        public static final String MAX_LA = "maxLa";
        public static final String MAX_CPU = "maxCpu";
        public static final String MAX_MEM = "maxMem";

        public static List<String> getProps()
        {
            return Lists.newArrayList(Constants.TIME, AVG_LA, AVG_CPU, AVG_MEM, MAX_LA, MAX_CPU, MAX_MEM);
        }
    }
}
