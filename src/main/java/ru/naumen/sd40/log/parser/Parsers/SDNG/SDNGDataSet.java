package ru.naumen.sd40.log.parser.Parsers.SDNG;

import com.google.common.collect.Lists;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;

import java.util.HashMap;
import java.util.List;

public class SDNGDataSet implements IDataSet {
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
    public static final String ADD_ACTIONS = "addActions";
    public static final String EDIT_ACTIONS = "editActions";
    public static final String LIST_ACTIONS = "listActions";
    public static final String COMMENT_ACTIONS = "commentActions";
    public static final String GET_FORM_ACTIONS = "getFormActions";
    public static final String GET_DT_OBJECT_ACTIONS = "getDtObjectActions";
    public static final String GET_CATALOGS_ACTIONS = "getCatalogsAction";
    public static final String SEARCH_ACTIONS = "searchActions";
    public static final String ACTIONS_COUNT = "count";

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

    public SDNGDataSet() {
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
    public HashMap<String, Object> getStat() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(COUNT, stat.getN());
        result.put(MIN, stat.getMin());
        result.put(MEAN, stat.getMean());
        result.put(STDDEV, stat.getStandardDeviation());
        result.put(PERCENTILE50, stat.getPercentile(50.0));
        result.put(PERCENTILE95, stat.getPercentile(95.0));
        result.put(PERCENTILE99, stat.getPercentile(99));
        result.put(PERCENTILE999, stat.getPercentile(99.9));
        result.put(MAX, stat.getMax());
        result.put(ERRORS, errorCounters.get(ErrorType.Error));
        result.put(ADD_ACTIONS, actionCounters.get(ActionType.AddObjectAction));
        result.put(EDIT_ACTIONS, actionCounters.get(ActionType.EditObjectsAction));
        result.put(LIST_ACTIONS, actionCounters.get(ActionType.GetListAction));
        result.put(COMMENT_ACTIONS, actionCounters.get(ActionType.CommentAction));
        result.put(GET_FORM_ACTIONS, actionCounters.get(ActionType.GetFormAction));
        result.put(GET_DT_OBJECT_ACTIONS, actionCounters.get(ActionType.GetDtObjectAction));
        result.put(GET_CATALOGS_ACTIONS, actionCounters.get(ActionType.GetCatalogsAction));
        result.put(SEARCH_ACTIONS, actionCounters.get(ActionType.SearchAction));

        return result;
    }

    @Override
    public List<String> getProps() {
        return Lists.newArrayList(COUNT, ERRORS, MEAN, STDDEV, MAX,
                PERCENTILE50, PERCENTILE95, PERCENTILE99, PERCENTILE999,
                ADD_ACTIONS, EDIT_ACTIONS, LIST_ACTIONS, COMMENT_ACTIONS, ACTIONS_COUNT,
                GET_FORM_ACTIONS, GET_DT_OBJECT_ACTIONS, GET_CATALOGS_ACTIONS, SEARCH_ACTIONS);
    }
}
