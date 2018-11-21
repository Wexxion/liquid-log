package ru.naumen.perfhouse.influx;

import static ru.naumen.perfhouse.statdata.Constants.GarbageCollection.AVARAGE_GC_TIME;
import static ru.naumen.perfhouse.statdata.Constants.GarbageCollection.GCTIMES;
import static ru.naumen.perfhouse.statdata.Constants.GarbageCollection.MAX_GC_TIME;
import static ru.naumen.perfhouse.statdata.Constants.PerformedActions.ADD_ACTIONS;
import static ru.naumen.perfhouse.statdata.Constants.PerformedActions.COMMENT_ACTIONS;
import static ru.naumen.perfhouse.statdata.Constants.PerformedActions.EDIT_ACTIONS;
import static ru.naumen.perfhouse.statdata.Constants.PerformedActions.GET_DT_OBJECT_ACTIONS;
import static ru.naumen.perfhouse.statdata.Constants.PerformedActions.GET_FORM_ACTIONS;
import static ru.naumen.perfhouse.statdata.Constants.PerformedActions.LIST_ACTIONS;
import static ru.naumen.perfhouse.statdata.Constants.PerformedActions.SEARCH_ACTIONS;
import static ru.naumen.perfhouse.statdata.Constants.PerformedActions.GET_CATALOGS_ACTIONS;
import static ru.naumen.perfhouse.statdata.Constants.ResponseTimes.*;
import static ru.naumen.perfhouse.statdata.Constants.Top.AVG_CPU;
import static ru.naumen.perfhouse.statdata.Constants.Top.AVG_LA;
import static ru.naumen.perfhouse.statdata.Constants.Top.AVG_MEM;
import static ru.naumen.perfhouse.statdata.Constants.Top.MAX_CPU;
import static ru.naumen.perfhouse.statdata.Constants.Top.MAX_LA;
import static ru.naumen.perfhouse.statdata.Constants.Top.MAX_MEM;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ru.naumen.perfhouse.statdata.Constants;
import ru.naumen.sd40.log.parser.Parsers.GC.GCDataSet;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;
import ru.naumen.sd40.log.parser.Parsers.SDNG.SDNGDataSet;
import ru.naumen.sd40.log.parser.Parsers.TOP.TopDataSet;

import static ru.naumen.sd40.log.parser.NumberUtils.prepareDouble;

/**
 * Created by doki on 24.10.16.
 */
@Component
public class InfluxDAO implements ILogStorage {
    private String influxHost;
    private String user;
    private String password;
    private InfluxDB influx;

    private String dbName;
    private boolean printTrace;

    @Autowired
    public InfluxDAO(@Value("${influx.host}") String influxHost, @Value("${influx.user}") String user,
                     @Value("${influx.password}") String password) {
        this.influxHost = influxHost;
        this.user = user;
        this.password = password;
        init();
    }

    public void createDb(String dbName, boolean printTrace) {
        this.printTrace = printTrace;
        this.dbName = dbName;
        influx.createDatabase(dbName);
    }

    @PreDestroy
    public void destroy() {
        influx.disableBatch();
    }

    public QueryResult.Series executeQuery(String dbName, String query) {
        Query q = new Query(query, dbName);
        QueryResult result = influx.query(q);

        if (result.getResults().get(0).getSeries() == null)
            return null;

        return result.getResults().get(0).getSeries().get(0);
    }

    public List<String> getDbList() {
        return influx.describeDatabases();
    }

    @PostConstruct
    public void init() {
        influx = InfluxDBFactory.connect(influxHost, user, password);
    }

    @Override
    public void saveDataSet(long date, IDataSet dataSet) {
        if(dataSet == null || dataSet.isNan())
            return;

        Builder builder;
        if (dataSet instanceof SDNGDataSet)
            builder = configureSDNG(date, (SDNGDataSet) dataSet);
        else if (dataSet instanceof TopDataSet)
            builder = configureTOP(date, (TopDataSet) dataSet);
        else if (dataSet instanceof GCDataSet)
            builder = configureGC(date, (GCDataSet) dataSet);
        else return;

        Point point = builder.build();
        influx.write(dbName, "autogen", point);
    }

    private Builder configureSDNG(long date, SDNGDataSet dataSet) {
        DescriptiveStatistics stat = dataSet.getFinalStat();
        long count = stat.getN();
        double min = stat.getMin();
        double mean = stat.getMean();
        double stddev = stat.getStandardDeviation();
        double max = stat.getMax();
        double percent50 = stat.getPercentile(50.0);
        double percent95 = stat.getPercentile(95.0);
        double percent99 = stat.getPercentile(99);
        double percent999 = stat.getPercentile(99.9);
        Integer errorCount = dataSet.getErrorCounters(SDNGDataSet.ErrorType.Error);
        if (printTrace) {
            System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n",
                    date, count, min, mean, stddev, percent50, percent95,
                    percent99, percent999, max, errorCount));
        }
        //@formatter:off
        return Point.measurement(Constants.MEASUREMENT_NAME).time(date, TimeUnit.MILLISECONDS)
                .addField(COUNT, count)
                .addField(MIN, min)
                .addField(MEAN, mean)
                .addField(STDDEV, stddev)
                .addField(PERCENTILE50, percent50)
                .addField(PERCENTILE95, percent95)
                .addField(PERCENTILE99, percent99)
                .addField(PERCENTILE999, percent999)
                .addField(MAX, max)
                .addField(ERRORS, errorCount)
                .addField(ADD_ACTIONS, dataSet.getActionCounters(SDNGDataSet.ActionType.AddObjectAction))
                .addField(EDIT_ACTIONS, dataSet.getActionCounters(SDNGDataSet.ActionType.EditObjectsAction))
                .addField(LIST_ACTIONS, dataSet.getActionCounters(SDNGDataSet.ActionType.GetListAction))
                .addField(COMMENT_ACTIONS, dataSet.getActionCounters(SDNGDataSet.ActionType.CommentAction))
                .addField(GET_FORM_ACTIONS, dataSet.getActionCounters(SDNGDataSet.ActionType.GetFormAction))
                .addField(GET_DT_OBJECT_ACTIONS, dataSet.getActionCounters(SDNGDataSet.ActionType.GetDtObjectAction))
                .addField(GET_CATALOGS_ACTIONS, dataSet.getActionCounters(SDNGDataSet.ActionType.GetCatalogsAction))
                .addField(SEARCH_ACTIONS, dataSet.getActionCounters(SDNGDataSet.ActionType.SearchAction));
    }

    private Builder configureGC(long date, GCDataSet dataSet) {
        DescriptiveStatistics stat = dataSet.getFinalStat();
        return Point.measurement(Constants.MEASUREMENT_NAME).time(date, TimeUnit.MILLISECONDS)
                .addField(GCTIMES, stat.getN()).addField(AVARAGE_GC_TIME, stat.getMean())
                .addField(MAX_GC_TIME, stat.getMax());
    }

    private Builder configureTOP(long date, TopDataSet dataSet) {
        DescriptiveStatistics laStat = dataSet.getFinalLaStat();
        DescriptiveStatistics memStat = dataSet.getFinalMemStat();
        DescriptiveStatistics cpuStat = dataSet.getFinalCpuStat();
        return Point.measurement(Constants.MEASUREMENT_NAME).time(date, TimeUnit.MILLISECONDS)
                .addField(AVG_LA, prepareDouble(laStat.getMean()))
                .addField(AVG_CPU, prepareDouble(cpuStat.getMean()))
                .addField(AVG_MEM, prepareDouble(memStat.getMean()))
                .addField(MAX_LA, prepareDouble(laStat.getMax()))
                .addField(MAX_CPU, prepareDouble(cpuStat.getMax()))
                .addField(MAX_MEM, prepareDouble(memStat.getMax()));
    }


    public BatchPoints startBatchPoints(String dbName) {
        return BatchPoints.database(dbName).build();
    }

    public void storeFromJSon(BatchPoints batch, String dbName, JSONObject data) {
        influx.createDatabase(dbName);
        long timestamp = (data.getLong("time"));
        long errors = (data.getLong("errors"));
        double p99 = (data.getDouble("tnn"));
        double p999 = (data.getDouble("tnnn"));
        double p50 = (data.getDouble("tmed"));
        double p95 = (data.getDouble("tn"));
        long count = (data.getLong("tcount"));
        double mean = (data.getDouble("avg"));
        double stddev = (data.getDouble("dev"));
        long max = (data.getLong("tmax"));
        long herrors = data.getLong("hErrors");

        Point measure = Point.measurement("perf").time(timestamp, TimeUnit.MILLISECONDS).addField("count", count)
                .addField("min", 0).addField("mean", mean).addField("stddev", stddev).addField("percent50", p50)
                .addField("percent95", p95).addField("percent99", p99).addField("percent999", p999).addField("max", max)
                .addField("errors", errors).addField("herrors", herrors).build();

        if (batch != null) {
            batch.getPoints().add(measure);
        } else {
            influx.write(dbName, "autogen", measure);
        }
    }
}
