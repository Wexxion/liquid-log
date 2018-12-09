package ru.naumen.sd40.log.parser.Parsers;

import java.util.HashMap;

public interface IDataSet {
    boolean isNan();
    HashMap<String, Object> getStat(boolean printTrace);
}
