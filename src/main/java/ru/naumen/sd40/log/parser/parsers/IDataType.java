package ru.naumen.sd40.log.parser.parsers;

import java.util.List;

public interface IDataType {
    List<String> getProps();
    String getPrefix();
    String getName();
}
