package ru.naumen.sd40.log.parser.parsers.front;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.IDataSet;
import ru.naumen.sd40.log.parser.parsers.IDataSetCreator;

@Component
public class FrontDataSetCreator implements IDataSetCreator {
    @Override
    public IDataSet create(long time) {
        return new FrontDataSet();
    }
}
