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

    public SDNGDataSet() {
        for(ActionType actionType : ActionType.values()){
            actionCounters.put(actionType, 0);
        }

        for(ErrorType errorType : ErrorType.values()){
            errorCounters.put(errorType, 0);
        }
    }

    private DescriptiveStatistics stat = new DescriptiveStatistics();
    private HashMap<ActionType, Integer> actionCounters = new HashMap<>();
    private HashMap<ErrorType, Integer> errorCounters = new HashMap<>();

    @Override
    public boolean isNan() {
        return stat.getN() == 0;
    }

    @Override
    public IDataSet create() {
        return new SDNGDataSet();
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

    public void incremetActionCounter(ActionType actionType) {
        actionCounters.put(actionType, actionCounters.get(actionType) + 1);
    }

    public Integer getErrorCounters(ErrorType errorType) {
        return errorCounters.get(errorType);
    }

    public void incremetErrorCounter(ErrorType errorType) {
        errorCounters.put(errorType, errorCounters.get(errorType) + 1);
    }
}
