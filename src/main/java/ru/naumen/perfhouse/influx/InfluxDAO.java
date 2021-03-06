package ru.naumen.perfhouse.influx;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.naumen.perfhouse.statdata.Constants;
import ru.naumen.sd40.log.parser.parsers.IDataSet;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        Point point = Point.measurement(Constants.MEASUREMENT_NAME)
                .time(date, TimeUnit.MILLISECONDS)
                .fields(dataSet.getStat(printTrace))
                .build();

        influx.write(dbName, "autogen", point);
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
