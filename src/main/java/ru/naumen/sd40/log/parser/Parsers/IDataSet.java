package ru.naumen.sd40.log.parser.Parsers;

import java.util.HashMap;
import java.util.List;

public interface IDataSet {
    boolean isNan();
    HashMap<String, Object> getStat();
    List<String> getProps();
}
