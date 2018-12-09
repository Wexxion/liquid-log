package ru.naumen.sd40.log.parser.Parsers.SDNG;

import com.google.common.collect.Lists;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.perfhouse.statdata.Constants;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;

import java.util.HashMap;
import java.util.List;

public class SDNGDataSet implements IDataSet {
    private final long time;

    public enum ActionType {
        AddObjectAction, EditObjectsAction, GetCatalogsAction,
        CommentAction, GetListAction, GetFormAction,
        GetDtObjectAction, SearchAction, EventAction,
    }

    public enum ErrorType {
        Warn, Error, Fatal
    }

    private final DescriptiveStatistics stat = new DescriptiveStatistics();
    private final HashMap<ActionType, Integer> actionCounters = new HashMap<>();
    private final HashMap<ErrorType, Integer> errorCounters = new HashMap<>();

    public SDNGDataSet(long time) {
        this.time = time;
        for(ActionType actionType : ActionType.values()){
            actionCounters.put(actionType, 0);
        }

        for(ErrorType errorType : ErrorType.values()){
            errorCounters.put(errorType, 0);
        }
    }

    @Override
    public boolean isNan() {
        return stat.getN() == 0;
    }

    void addTimes(Integer times) {
        stat.addValue(times);
    }

    void incrementActionCounter(ActionType actionType) {
        actionCounters.put(actionType, actionCounters.get(actionType) + 1);
    }

    void incrementErrorCounter(ErrorType errorType) {
        errorCounters.put(errorType, errorCounters.get(errorType) + 1);
    }

    @Override
    public HashMap<String, Object> getStat(boolean printTrace) {
        HashMap<String, Object> result = new HashMap<>();

        long count = stat.getN();
        double min = stat.getMin();
        double mean = stat.getMean();
        double stddev = stat.getStandardDeviation();
        double max = stat.getMax();
        double percent50 = stat.getPercentile(50.0);
        double percent95 = stat.getPercentile(95.0);
        double percent99 = stat.getPercentile(99);
        double percent999 = stat.getPercentile(99.9);
        Integer errorCount = errorCounters.get(SDNGDataSet.ErrorType.Error);

        if (printTrace) {
            System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n",
                    time, count, min, mean, stddev, percent50, percent95,
                    percent99, percent999, max, errorCount));
        }

        result.put(Fields.ResponseTimes.COUNT, stat.getN());
        result.put(Fields.ResponseTimes.MIN, stat.getMin());
        result.put(Fields.ResponseTimes.MEAN, stat.getMean());
        result.put(Fields.ResponseTimes.STDDEV, stat.getStandardDeviation());
        result.put(Fields.ResponseTimes.PERCENTILE50, stat.getPercentile(50.0));
        result.put(Fields.ResponseTimes.PERCENTILE95, stat.getPercentile(95.0));
        result.put(Fields.ResponseTimes.PERCENTILE99, stat.getPercentile(99));
        result.put(Fields.ResponseTimes.PERCENTILE999, stat.getPercentile(99.9));
        result.put(Fields.ResponseTimes.MAX, stat.getMax());
        result.put(Fields.ResponseTimes.ERRORS, errorCounters.get(ErrorType.Error));
        result.put(Fields.PerformedActions.ADD_ACTIONS, actionCounters.get(ActionType.AddObjectAction));
        result.put(Fields.PerformedActions.EDIT_ACTIONS, actionCounters.get(ActionType.EditObjectsAction));
        result.put(Fields.PerformedActions.LIST_ACTIONS, actionCounters.get(ActionType.GetListAction));
        result.put(Fields.PerformedActions.COMMENT_ACTIONS, actionCounters.get(ActionType.CommentAction));
        result.put(Fields.PerformedActions.GET_FORM_ACTIONS, actionCounters.get(ActionType.GetFormAction));
        result.put(Fields.PerformedActions.GET_DT_OBJECT_ACTIONS, actionCounters.get(ActionType.GetDtObjectAction));
        result.put(Fields.PerformedActions.GET_CATALOGS_ACTIONS, actionCounters.get(ActionType.GetCatalogsAction));
        result.put(Fields.PerformedActions.SEARCH_ACTIONS, actionCounters.get(ActionType.SearchAction));

        return result;
    }

    public static class Fields {
        public static class ResponseTimes
        {
            public static final String PERCENTILE50 = "percent50";
            public static final String PERCENTILE95 = "percent95";
            public static final String PERCENTILE99 = "percent99";
            public static final String PERCENTILE999 = "percent999";
            public static final String MAX = "max";
            public static final String MIN = "min";
            public static final String COUNT = "count";
            public static final String ERRORS = "errors";
            public static final String MEAN = "mean";
            public static final String STDDEV = "stddev";

            public static List<String> getProps()
            {
                return Lists.newArrayList(Constants.TIME, COUNT, ERRORS, MEAN, STDDEV, PERCENTILE50, PERCENTILE95, PERCENTILE99,
                        PERCENTILE999, MAX);
            }
        }

        public static class PerformedActions
        {
            public static final String ADD_ACTIONS = "addActions";
            public static final String EDIT_ACTIONS = "editActions";
            public static final String LIST_ACTIONS = "listActions";
            public static final String COMMENT_ACTIONS = "commentActions";
            public static final String GET_FORM_ACTIONS = "getFormActions";
            public static final String GET_DT_OBJECT_ACTIONS = "getDtObjectActions";
            public static final String GET_CATALOGS_ACTIONS = "getCatalogsAction";
            public static final String SEARCH_ACTIONS = "searchActions";
            public static final String ACTIONS_COUNT = "count";

            public static List<String> getProps()
            {
                return Lists.newArrayList(Constants.TIME, ADD_ACTIONS, EDIT_ACTIONS, LIST_ACTIONS, COMMENT_ACTIONS, ACTIONS_COUNT,
                        GET_FORM_ACTIONS, GET_DT_OBJECT_ACTIONS, GET_CATALOGS_ACTIONS, SEARCH_ACTIONS);
            }

        }
    }
}
