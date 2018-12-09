package ru.naumen.perfhouse.statdata;

import ru.naumen.sd40.log.parser.Parsers.GC.GCDataSet;
import ru.naumen.sd40.log.parser.Parsers.SDNG.SDNGDataSet;
import ru.naumen.sd40.log.parser.Parsers.TOP.TopDataSet;

import java.util.List;

public enum DataType
{
    GARBAGE_COLLECTION(GCDataSet.Fields.getProps()),
    RESPONSE(SDNGDataSet.Fields.ResponseTimes.getProps()),
    ACTIONS(SDNGDataSet.Fields.PerformedActions.getProps()),
    TOP(TopDataSet.Fields.getProps());
    
    private List<String> properties;

    DataType(List<String> properties) { this.properties = properties; }

    List<String> getTypeProperties() { return this.properties; }
}
