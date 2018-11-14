package ru.naumen.sd40.log.parser.Parsers.GC;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;
import ru.naumen.sd40.log.parser.Parsers.IDataSetCreator;

@Component
class GCDataSetCreator implements IDataSetCreator {
    @Override
    public IDataSet Create() {
        return new GCDataSet();
    }

    @Override
    public String getModeName() {
        return "gc";
    }
}
