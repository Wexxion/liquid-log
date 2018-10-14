package ru.naumen.sd40.log.parser.Parsers.SDNG;

import ru.naumen.sd40.log.parser.DataSet;
import ru.naumen.sd40.log.parser.Parsers.IDataParser;

public class SDNGDataParser implements IDataParser {
    @Override
    public void ParseLine(DataSet dataSet, String line) {
        dataSet.parseLine(line);
    }
}
