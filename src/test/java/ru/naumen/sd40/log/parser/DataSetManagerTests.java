package ru.naumen.sd40.log.parser;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.naumen.perfhouse.influx.ILogSaver;

public class DataSetManagerTests {
    @Test
    public void mustReturnOldDataSetWhenOldKey() {
        //given
        ILogSaver influx = Mockito.mock(ILogSaver.class);
        DataSetManager dataSetService = new DataSetManager(influx, null, true);
        long key = 5;
        DataSet expectedDataSet = dataSetService.getDataSet(key);
        //when
        DataSet currentDataSet = dataSetService.getDataSet(key);
        //then
        Assert.assertEquals(expectedDataSet, currentDataSet);
    }
    @Test
    public void mustReturnNewDataSetWhenNewKey() {
        //given
        ILogSaver influx = Mockito.mock(ILogSaver.class);
        DataSetManager dataSetService = new DataSetManager(influx, null, true);
        long key = 5;
        DataSet oldDataSet = dataSetService.getDataSet(key);
        //when
        key = 1337;
        DataSet currentDataSet = dataSetService.getDataSet(key);
        //then
        Assert.assertNotEquals(oldDataSet, currentDataSet);
    }
}
