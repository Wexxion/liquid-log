package ru.naumen.sd40.log.parser;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.naumen.perfhouse.influx.IDatabase;

public class DatabaseTests {
    @Test
    public void mustReturnOldDataSetWhenOldKey() {
        //given
        IDatabase influx = Mockito.mock(IDatabase.class);
        Database dataSetService = new Database(influx, null, true);
        long key = 5;
        DataSet expectedDataSet = dataSetService.get(key);
        //when
        DataSet currentDataSet = dataSetService.get(key);
        //then
        Assert.assertEquals(expectedDataSet, currentDataSet);
    }
    @Test
    public void mustReturnNewDataSetWhenNewKey() {
        //given
        IDatabase influx = Mockito.mock(IDatabase.class);
        Database dataSetService = new Database(influx, null, true);
        long key = 5;
        DataSet oldDataSet = dataSetService.get(key);
        //when
        key = 1337;
        DataSet currentDataSet = dataSetService.get(key);
        //then
        Assert.assertNotEquals(oldDataSet, currentDataSet);
    }
}
