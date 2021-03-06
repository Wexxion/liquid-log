package ru.naumen.sd40.log.parser;

import ru.naumen.perfhouse.influx.ILogStorage;
import ru.naumen.sd40.log.parser.parsers.IDataSet;
import ru.naumen.sd40.log.parser.parsers.IDataSetCreator;

class DataSetManager {
    private final ILogStorage logSaver;
    private final IDataSetCreator dataSetCreator;

    private long currentTime;
    private IDataSet currentDataSet;

    DataSetManager(ILogStorage logSaver, IDataSetCreator dataSetCreator, String dbName, boolean printTrace) {
        logSaver.createDb(dbName, printTrace);
        this.logSaver = logSaver;
        this.dataSetCreator = dataSetCreator;
    }

    IDataSet getDataSet(long key) {
        if (key == currentTime)
            return currentDataSet;

        logSaver.saveDataSet(currentTime, currentDataSet);

        currentTime = key;
        currentDataSet = dataSetCreator.create(currentTime);
        return currentDataSet;
    }
}
