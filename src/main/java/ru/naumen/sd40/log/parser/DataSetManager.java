package ru.naumen.sd40.log.parser;

import ru.naumen.perfhouse.influx.ILogStorage;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;
import ru.naumen.sd40.log.parser.Parsers.IDataSetCreator;

class DataSetManager {
    private ILogStorage logSaver;
    private IDataSetCreator dataSetCreator;

    private long currentTime;
    private IDataSet currentDataSet;

    DataSetManager(ILogStorage logSaver, IDataSetCreator dataSetCreator,
                   String dbName, boolean printTrace) {
        logSaver.createDb(dbName, printTrace);
        this.logSaver = logSaver;
        this.dataSetCreator = dataSetCreator;
    }

    IDataSet getDataSet(long key) {
        if (key == currentTime)
            return currentDataSet;

        logSaver.saveDataSet(currentTime, currentDataSet);

        currentTime = key;
        currentDataSet = dataSetCreator.Create();
        return currentDataSet;
    }
}
