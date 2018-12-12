package ru.naumen.sd40.log.parser.parsers.gc;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.IDataSet;
import ru.naumen.sd40.log.parser.parsers.IDataSetCreator;

@Component
class GCDataSetCreator implements IDataSetCreator {
    @Override
    public IDataSet create(long time) {
        return new GCDataSet();
    }
}
