package ru.naumen.perfhouse.influx;

import ru.naumen.sd40.log.parser.parsers.IDataSet;

public interface ILogStorage {
    void createDb(String dbName, boolean printTrace);
    void saveDataSet(long date, IDataSet dataSet);
}
