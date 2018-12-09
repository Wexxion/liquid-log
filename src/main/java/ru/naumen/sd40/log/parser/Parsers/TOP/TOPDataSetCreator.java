package ru.naumen.sd40.log.parser.Parsers.TOP;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;
import ru.naumen.sd40.log.parser.Parsers.IDataSetCreator;

@Component
class TOPDataSetCreator implements IDataSetCreator {
    @Override
    public IDataSet create(long time) {
        return new TopDataSet();
    }
}
