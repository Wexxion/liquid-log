package ru.naumen.perfhouse.influx;

import org.influxdb.dto.BatchPoints;
import ru.naumen.sd40.log.parser.*;

public interface IDatabase {
    void init();
    void connectToDB(String dbName);

    void storeActionsFromLog(String dbName, long date, ActionDoneParser dones, ErrorParser errors);
    void storeGc(String dbName, long date, GCParser gc);
    void storeTop(String dbName, long date, TopData data);

}
