package ru.naumen.sd40.log.parser;

import ru.naumen.perfhouse.influx.ILogSaver;

public class DataSetManager {
    private ILogSaver logSaver;
    private boolean noCsv;

    private long currentTime;
    private DataSet currentDataSet;

    public DataSetManager(ILogSaver logSaver, String dbName, boolean noCsv) {
        logSaver.createDb(dbName);
        this.logSaver = logSaver;
        this.noCsv = noCsv;
    }

    DataSet getDataSet(long key) {
        if (key == currentTime)
            return currentDataSet;

        saveCurrentDataSet();

        currentTime = key;
        currentDataSet = new DataSet();
        return currentDataSet;
    }

    private void saveCurrentDataSet() {
        if (currentDataSet == null) {
            if (!noCsv)
                System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
            return;
        }

        ErrorParser erros = currentDataSet.getErrors();
        ActionDoneParser dones = currentDataSet.getActionsDone();
        dones.calculate();
        if (!noCsv) {
            System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", currentTime, dones.getCount(),
                    dones.getMin(), dones.getMean(), dones.getStddev(), dones.getPercent50(), dones.getPercent95(),
                    dones.getPercent99(), dones.getPercent999(), dones.getMax(), erros.getErrorCount()));
        }

        if (!dones.isNan())
            logSaver.saveActionsFromLog(currentTime, dones, erros);

        GCParser gc = currentDataSet.getGc();
        if (!gc.isNan())
            logSaver.saveGc(currentTime, gc);

        TopData cpuData = currentDataSet.cpuData();
        if (!cpuData.isNan())
            logSaver.saveTop(currentTime, cpuData);
    }
}
