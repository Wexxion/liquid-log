package ru.naumen.sd40.log.parser.parsers.sdng;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.IDataType;

import java.util.List;

@Component
public class PerformedActionsDataType implements IDataType {
    public static final String ADD_ACTIONS = "addActions";
    public static final String EDIT_ACTIONS = "editActions";
    public static final String LIST_ACTIONS = "listActions";
    public static final String COMMENT_ACTIONS = "commentActions";
    public static final String GET_FORM_ACTIONS = "getFormActions";
    public static final String GET_DT_OBJECT_ACTIONS = "getDtObjectActions";
    public static final String GET_CATALOGS_ACTIONS = "getCatalogsAction";
    public static final String SEARCH_ACTIONS = "searchActions";
    public static final String ACTIONS_COUNT = "count";

    @Override
    public List<String> getProps() {
        return Lists.newArrayList(ADD_ACTIONS, EDIT_ACTIONS, LIST_ACTIONS, COMMENT_ACTIONS, ACTIONS_COUNT,
                GET_FORM_ACTIONS, GET_DT_OBJECT_ACTIONS, GET_CATALOGS_ACTIONS, SEARCH_ACTIONS);
    }

    @Override
    public String getPrefix() {
        return "actions";
    }

    @Override
    public String getName() {
        return "Performed actions";
    }
}
