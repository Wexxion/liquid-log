package ru.naumen.sd40.log.parser;

import org.influxdb.dto.BatchPoints;
import ru.naumen.perfhouse.influx.IDatabase;

import java.util.HashMap;

public class Database {
    private IDatabase influx;
    private String dbName;
    private boolean noCsv;

    private long currentTime;
    private DataSet currentDataSet;

    public Database(IDatabase influx, String dbName, boolean noCsv) {
        influx.init();
        influx.connectToDB(dbName);
        this.influx = influx;
        this.dbName = dbName;
        this.noCsv = noCsv;
    }

    DataSet get(long key) {
        if (key == currentTime)
            return currentDataSet;

        saveToDb();

        currentTime = key;
        currentDataSet = new DataSet();
        return currentDataSet;
    }

    private void saveToDb() {
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

//        if (!dones.isNan())
//            influx.storeActionsFromLog(dbName, currentTime, dones, erros);
//
//        GCParser gc = currentDataSet.getGc();
//        if (!gc.isNan())
//            influx.storeGc(dbName, currentTime, gc);
//
//        TopData cpuData = currentDataSet.cpuData();
//        if (!cpuData.isNan())
//            influx.storeTop(dbName, currentTime, cpuData);
    }
}
