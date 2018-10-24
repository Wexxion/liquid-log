package ru.naumen.perfhouse.influx;

import ru.naumen.sd40.log.parser.ActionDoneParser;
import ru.naumen.sd40.log.parser.ErrorParser;
import ru.naumen.sd40.log.parser.GCParser;
import ru.naumen.sd40.log.parser.TopData;

public interface ILogSaver {
    void createDb(String dbName);

    void saveActionsFromLog(long date, ActionDoneParser dones, ErrorParser errors);
    void saveGc(long date, GCParser gc);
    void saveTop(long date, TopData data);

}
