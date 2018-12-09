package ru.naumen.sd40.log.parser.Parsers.SDNG;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;
import ru.naumen.sd40.log.parser.Parsers.IDataSetCreator;

@Component
class SDNGDataSetCreator implements IDataSetCreator {
    @Override
    public IDataSet create(long time) {
        return new SDNGDataSet(time);
    }
}
