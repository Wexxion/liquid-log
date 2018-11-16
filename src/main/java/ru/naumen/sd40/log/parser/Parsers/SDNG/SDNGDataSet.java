package ru.naumen.sd40.log.parser.Parsers.SDNG;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;

import java.util.HashMap;

public class SDNGDataSet implements IDataSet {
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

    public DescriptiveStatistics getFinalStat() {
        return stat.copy();
    }

    public void addTimes(Integer times) {
        stat.addValue(times);
    }

    public Integer getActionCounters(ActionType actionType) {
        return actionCounters.get(actionType);
    }

    public void incrementActionCounter(ActionType actionType) {
        actionCounters.put(actionType, actionCounters.get(actionType) + 1);
    }

    public Integer getErrorCounters(ErrorType errorType) {
        return errorCounters.get(errorType);
    }

    public void incrementErrorCounter(ErrorType errorType) {
        errorCounters.put(errorType, errorCounters.get(errorType) + 1);
    }
}
