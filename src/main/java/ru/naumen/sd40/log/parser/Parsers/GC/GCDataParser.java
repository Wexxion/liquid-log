package ru.naumen.sd40.log.parser.Parsers.GC;

import ru.naumen.sd40.log.parser.DataSet;
import ru.naumen.sd40.log.parser.Parsers.IDataParser;

public class GCDataParser implements IDataParser {
    @Override
    public void ParseLine(DataSet dataSet, String line) {
        dataSet.parseGcLine(line);
    }
}
