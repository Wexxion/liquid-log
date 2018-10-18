package ru.naumen.sd40.log.parser.Parsers;

import ru.naumen.sd40.log.parser.DataSet;

public interface IDataParser {
    public void ParseLine(DataSet dataSet, String line);
}
