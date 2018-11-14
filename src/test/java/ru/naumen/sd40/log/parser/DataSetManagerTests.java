package ru.naumen.sd40.log.parser;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.naumen.perfhouse.influx.ILogStorage;
import ru.naumen.sd40.log.parser.Parsers.IDataSet;
import ru.naumen.sd40.log.parser.Parsers.IDataSetCreator;

public class DataSetManagerTests {
    @Test
    public void mustReturnOldDataSetWhenOldKey() {
        //given
        ILogStorage influx = Mockito.mock(ILogStorage.class);
        IDataSetCreator dataSetCreator = Mockito.mock(IDataSetCreator.class);
        DataSetManager dataSetService = new DataSetManager(influx, dataSetCreator, null, true);
        long key = 5;
        IDataSet expectedDataSet = dataSetService.getDataSet(key);
        //when
        IDataSet currentDataSet = dataSetService.getDataSet(key);
        //then
        Assert.assertEquals(expectedDataSet, currentDataSet);
    }
    @Test
    public void mustReturnNewDataSetWhenNewKey() {
        //given
        ILogStorage influx = Mockito.mock(ILogStorage.class);
        IDataSetCreator dataSetCreator = Mockito.mock(IDataSetCreator.class);
        DataSetManager dataSetService = new DataSetManager(influx, dataSetCreator, null, true);
        long key = 5;
        IDataSet oldDataSet = dataSetService.getDataSet(key);
        //when
        key = 1337;
        IDataSet currentDataSet = dataSetService.getDataSet(key);
        //then
        Assert.assertNotEquals(oldDataSet, currentDataSet);
    }
}
