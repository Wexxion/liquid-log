package ru.naumen.sd40.log.parser.parsers.front;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.IDataType;

import java.util.List;

@Component
public class FrontDataType implements IDataType {
    public static final String FRONT_TIMES = "renderTimes";
    public static final String MIN_RENDER_TIME = "minRenderTime";
    public static final String AVERAGE_RENDER_TIME = "avgRenderTime";
    public static final String MAX_RENDER_TIME = "maxRenderTime";

    @Override
    public List<String> getProps()  {
        return Lists.newArrayList(FRONT_TIMES, MIN_RENDER_TIME, AVERAGE_RENDER_TIME, MAX_RENDER_TIME);
    }

    @Override
    public String getPrefix() {
        return "front";
    }

    @Override
    public String getName() {
        return "Front render";
    }
}
