package ru.naumen.sd40.log.parser.Parsers;

public interface IDataParser<DataSet extends IDataSet> {
    void parseLine(DataSet dataSet, String line);
    void configureViaSettings(ParserSettings settings);
}
