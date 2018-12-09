package ru.naumen.sd40.log.parser;

import org.junit.Assert;
import org.junit.Test;
import ru.naumen.sd40.log.parser.Parsers.IDataParser;
import ru.naumen.sd40.log.parser.Parsers.SDNG.SDNGDataParser;
import ru.naumen.sd40.log.parser.Parsers.SDNG.SDNGDataSet;
import ru.naumen.sd40.log.parser.Parsers.SDNG.SDNGLogParser;
import ru.naumen.sd40.log.parser.Parsers.SDNG.SDNGTimeParser;

public class ActionDoneParserTest {

    @Test
    public void mustParseAddAction() {
        //given
        SDNGDataSet dataSet = new SDNGDataSet();
        IDataParser parser = new SDNGDataParser();

        //when
        parser.parseLine(dataSet,"Done(10): AddObjectAction");

        //then
        Assert.assertEquals(1, (int)(dataSet.getStat().get(SDNGDataSet.ADD_ACTIONS)));
    }

    @Test
    public void mustParseFormActions() {
        //given
        SDNGDataSet dataSet = new SDNGDataSet();
        IDataParser parser = new SDNGDataParser();

        //when
        parser.parseLine(dataSet,"Done(10): GetFormAction");
        parser.parseLine(dataSet,"Done(1): GetAddFormContextDataAction");

        //then
        Assert.assertEquals(2, (int)(dataSet.getStat().get(SDNGDataSet.GET_FORM_ACTIONS)));
    }

    @Test
    public void mustParseEditObject() {
        //given
        SDNGDataSet dataSet = new SDNGDataSet();
        IDataParser parser = new SDNGDataParser();

        //when
        parser.parseLine(dataSet,"Done(10): EditObjectAction");

        //then
        Assert.assertEquals(1, (int)(dataSet.getStat().get(SDNGDataSet.EDIT_ACTIONS)));
    }

    @Test
    public void mustParseSearchObject(){
        //given
        SDNGDataSet dataSet = new SDNGDataSet();
        IDataParser parser = new SDNGDataParser();

        //when
        parser.parseLine(dataSet,"Done(10): GetPossibleAgreementsChildsSearchAction");
        parser.parseLine(dataSet,"Done(10): TreeSearchAction");
        parser.parseLine(dataSet,"Done(10): GetSearchResultAction");
        parser.parseLine(dataSet,"Done(10): GetSimpleSearchResultsAction");
        parser.parseLine(dataSet,"Done(10): SimpleSearchAction");
        parser.parseLine(dataSet,"Done(10): ExtendedSearchByStringAction");
        parser.parseLine(dataSet,"Done(10): ExtendedSearchByFilterAction");

        //then
        Assert.assertEquals(7, (int)(dataSet.getStat().get(SDNGDataSet.SEARCH_ACTIONS)));
    }

    @Test
    public void mustParseGetList(){
        //given
        SDNGDataSet dataSet = new SDNGDataSet();
        IDataParser parser = new SDNGDataParser();

        //when
        parser.parseLine(dataSet, "Done(10): GetDtObjectListAction");
        parser.parseLine(dataSet, "Done(10): GetPossibleCaseListValueAction");
        parser.parseLine(dataSet, "Done(10): GetPossibleAgreementsTreeListActions");
        parser.parseLine(dataSet, "Done(10): GetCountForObjectListAction");
        parser.parseLine(dataSet, "Done(10): GetDataForObjectListAction");
        parser.parseLine(dataSet, "Done(10): GetPossibleAgreementsListActions");
        parser.parseLine(dataSet, "Done(10): GetDtObjectForRelObjListAction");

        //then
        Assert.assertEquals(7, (int)(dataSet.getStat().get(SDNGDataSet.LIST_ACTIONS)));
    }

    @Test
    public void mustParseComment(){
        //given
        SDNGDataSet dataSet = new SDNGDataSet();
        IDataParser parser = new SDNGDataParser();

        //when
        parser.parseLine(dataSet, "Done(10): EditCommentAction");
        parser.parseLine(dataSet, "Done(10): ChangeResponsibleWithAddCommentAction");
        parser.parseLine(dataSet, "Done(10): ShowMoreCommentAttrsAction");
        parser.parseLine(dataSet, "Done(10): CheckObjectsExceedsCommentsAmountAction");
        parser.parseLine(dataSet, "Done(10): GetAddCommentPermissionAction");
        parser.parseLine(dataSet, "Done(10): GetCommentDtObjectTemplateAction");

        //then
        Assert.assertEquals(6, (int)(dataSet.getStat().get(SDNGDataSet.COMMENT_ACTIONS)));
    }

    @Test
    public void mustParseDtObject(){
        //given
        SDNGDataSet dataSet = new SDNGDataSet();
        IDataParser parser = new SDNGDataParser();

        //when
        parser.parseLine(dataSet, "Done(10): GetVisibleDtObjectAction");
        parser.parseLine(dataSet, "Done(10): GetDtObjectsAction");
        parser.parseLine(dataSet, "Done(10): GetDtObjectTreeSelectionStateAction");
        parser.parseLine(dataSet, "Done(10): AbstractGetDtObjectTemplateAction");
        parser.parseLine(dataSet, "Done(10): GetDtObjectTemplateAction");

        //then
        Assert.assertEquals(5, (int)(dataSet.getStat().get(SDNGDataSet.GET_DT_OBJECT_ACTIONS)));
    }

    @Test
    public void mustParseCatalogs(){
        //given
        SDNGDataSet dataSet = new SDNGDataSet();
        IDataParser parser = new SDNGDataParser();

        //when
        parser.parseLine(dataSet, "Done(113):GetCatalogsAction");
        parser.parseLine(dataSet, "SQL(0) Done(113):GetCatalogsAction");
        parser.parseLine(dataSet, "SQL(0) Done(41):GetCatalogsAction [getCodes()=null, ]");
        parser.parseLine(dataSet, "Done(777):GetCatalogAction");

        //then
        Assert.assertEquals(3, (int)(dataSet.getStat().get(SDNGDataSet.GET_CATALOGS_ACTIONS)));
    }
}
