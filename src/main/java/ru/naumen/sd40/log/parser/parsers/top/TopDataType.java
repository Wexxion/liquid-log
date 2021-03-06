package ru.naumen.sd40.log.parser.parsers.top;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.IDataType;

import java.util.List;

@Component
public class TopDataType implements IDataType {
    public static final String AVG_LA = "avgLa";
    public static final String AVG_CPU = "avgCpu";
    public static final String AVG_MEM = "avgMem";
    public static final String MAX_LA = "maxLa";
    public static final String MAX_CPU = "maxCpu";
    public static final String MAX_MEM = "maxMem";

    @Override
    public List<String> getProps() {
        return Lists.newArrayList(AVG_LA, AVG_CPU, AVG_MEM, MAX_LA, MAX_CPU, MAX_MEM);
    }

    @Override
    public String getPrefix() {
        return "top";
    }

    @Override
    public String getName() {
        return "Top Data";
    }
}
