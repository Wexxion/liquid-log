package ru.naumen.sd40.log.parser.parsers.top;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.IDataSet;
import ru.naumen.sd40.log.parser.parsers.IDataSetCreator;

@Component
class TOPDataSetCreator implements IDataSetCreator {
    @Override
    public IDataSet create(long time) {
        return new TopDataSet();
    }
}
