package ru.naumen.sd40.log.parser.Parsers.SDNG;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;

import java.util.HashMap;

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

        result.put(ResponseTimesDataType.COUNT, stat.getN());
        result.put(ResponseTimesDataType.MIN, stat.getMin());
        result.put(ResponseTimesDataType.MEAN, stat.getMean());
        result.put(ResponseTimesDataType.STDDEV, stat.getStandardDeviation());
        result.put(ResponseTimesDataType.PERCENTILE50, stat.getPercentile(50.0));
        result.put(ResponseTimesDataType.PERCENTILE95, stat.getPercentile(95.0));
        result.put(ResponseTimesDataType.PERCENTILE99, stat.getPercentile(99));
        result.put(ResponseTimesDataType.PERCENTILE999, stat.getPercentile(99.9));
        result.put(ResponseTimesDataType.MAX, stat.getMax());
        result.put(ResponseTimesDataType.ERRORS, errorCounters.get(ErrorType.Error));
        result.put(PerformedActionsDataType.ADD_ACTIONS, actionCounters.get(ActionType.AddObjectAction));
        result.put(PerformedActionsDataType.EDIT_ACTIONS, actionCounters.get(ActionType.EditObjectsAction));
        result.put(PerformedActionsDataType.LIST_ACTIONS, actionCounters.get(ActionType.GetListAction));
        result.put(PerformedActionsDataType.COMMENT_ACTIONS, actionCounters.get(ActionType.CommentAction));
        result.put(PerformedActionsDataType.GET_FORM_ACTIONS, actionCounters.get(ActionType.GetFormAction));
        result.put(PerformedActionsDataType.GET_DT_OBJECT_ACTIONS, actionCounters.get(ActionType.GetDtObjectAction));
        result.put(PerformedActionsDataType.GET_CATALOGS_ACTIONS, actionCounters.get(ActionType.GetCatalogsAction));
        result.put(PerformedActionsDataType.SEARCH_ACTIONS, actionCounters.get(ActionType.SearchAction));

        return result;
    }
}
