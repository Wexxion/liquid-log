package ru.naumen.sd40.log.parser.parsers.sdng;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.IDataSet;
import ru.naumen.sd40.log.parser.parsers.IDataSetCreator;

@Component
class SDNGDataSetCreator implements IDataSetCreator {
    @Override
    public IDataSet create(long time) {
        return new SDNGDataSet(time);
    }
}
