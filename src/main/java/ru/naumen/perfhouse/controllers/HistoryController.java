package ru.naumen.perfhouse.controllers;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ru.naumen.perfhouse.statdata.StatData;
import ru.naumen.perfhouse.statdata.StatDataService;
import ru.naumen.sd40.log.parser.Parsers.IDataType;

import javax.inject.Inject;

/**
 * Created by doki on 23.10.16.
 */
@Controller
public class HistoryController
{
    private static final String NO_HISTORY_VIEW = "no_history";
    private final StatDataService service;
    private final HashMap<String, IDataType> dataTypes;
    private IDataType defaultDataType = null;

    @Inject
    public HistoryController(StatDataService service, List<IDataType> dataTypes)
    {
        this.service = service;
        this.dataTypes = new HashMap<>();

        for (IDataType dataType: dataTypes)
            this.dataTypes.put(dataType.getPrefix(), dataType);

        if(!dataTypes.isEmpty())
            this.defaultDataType = dataTypes.get(0);
    }

    @RequestMapping(path = "/history/{client}/{year}/{month}/{day}")
    public ModelAndView indexByDay(@PathVariable("client") String client,
            @PathVariable(name = "year", required = false) int year,
            @PathVariable(name = "month", required = false) int month,
            @PathVariable(name = "day", required = false) int day) throws ParseException
    {
        return getDataAndViewByDate(client, defaultDataType, year, month, day);
    }

    @RequestMapping(path = "/history/{client}/{type}/{year}/{month}/{day}")
    public ModelAndView customByDay(@PathVariable("client") String client,
                                    @PathVariable("type") String type,
                                    @PathVariable(name = "year", required = false) int year,
                                    @PathVariable(name = "month", required = false) int month,
                                    @PathVariable(name = "day", required = false) int day) throws ParseException
    {
        return getDataAndViewByDate(client, dataTypes.getOrDefault(type, null), year, month, day);
    }

    @RequestMapping(path = "/history/{client}/{year}/{month}")
    public ModelAndView indexByMonth(@PathVariable("client") String client,
            @PathVariable(name = "year", required = false) int year,
            @PathVariable(name = "month", required = false) int month) throws ParseException
    {
        return getDataAndViewByDate(client, defaultDataType, year, month, 0, true);
    }

    @RequestMapping(path = "/history/{client}/{type}/{year}/{month}")
    public ModelAndView customByMonth(@PathVariable("client") String client,
                                      @PathVariable("type") String type,
                                      @PathVariable(name = "year", required = false) int year,
                                      @PathVariable(name = "month", required = false) int month) throws ParseException
    {
        return getDataAndViewByDate(client, dataTypes.getOrDefault(type, null), year, month, 0, true);
    }

    @RequestMapping(path = "/history/{client}")
    public ModelAndView indexLast864(@PathVariable("client") String client,
            @RequestParam(name = "count", defaultValue = "864") int count) throws ParseException
    {
        if(defaultDataType == null) return new ModelAndView(NO_HISTORY_VIEW);
        ru.naumen.perfhouse.statdata.StatData d = service.getData(client, defaultDataType, count);
        if (d == null) return new ModelAndView(NO_HISTORY_VIEW);

        Map<String, Object> model = new HashMap<>(d.asModel());
        model.put("client", client);
        model.put("dataTypes", dataTypes.values());

        return new ModelAndView(String.format("charts/%s", defaultDataType.getPrefix()), model, HttpStatus.OK);
    }

    @RequestMapping(path = "/history/{client}/{type}")
    public ModelAndView customIndex(@PathVariable("client") String client,
                                    @PathVariable("type") String type,
                                    @RequestParam("from") String from,
                                    @RequestParam("to") String to,
                                    @RequestParam("maxResults") int maxResults) throws ParseException
    {
        return getDataAndViewCustom(client, dataTypes.getOrDefault(type, null), from, to, maxResults);
    }

    private ModelAndView getDataAndViewByDate(String client, IDataType type, int year, int month, int day) throws ParseException
    {
        return getDataAndViewByDate(client, type, year, month, day, false);
    }

    private ModelAndView getDataAndViewByDate(String client, IDataType type, int year, int month, int day, boolean compress) throws ParseException
    {
        if(type == null) return new ModelAndView(NO_HISTORY_VIEW);
        ru.naumen.perfhouse.statdata.StatData dataDate = service.getDataDate(client, type, year, month, day);
        if (dataDate == null) return new ModelAndView(NO_HISTORY_VIEW);

        dataDate = compress ? service.compress(dataDate, 3 * 60 * 24 / 5) : dataDate;
        Map<String, Object> model = new HashMap<>(dataDate.asModel());
        model.put("client", client);
        model.put("year", year);
        model.put("month", month);
        model.put("day", day);
        model.put("dataTypes", dataTypes.values());
        return new ModelAndView(String.format("charts/%s", type.getPrefix()), model, HttpStatus.OK);
    }

    private ModelAndView getDataAndViewCustom(String client, IDataType dataType, String from, String to, int maxResults) throws ParseException
    {
        if(dataType == null) return new ModelAndView(NO_HISTORY_VIEW);
        StatData data = service.getDataCustom(client, dataType, from, to);
        if (data == null)
            return new ModelAndView(NO_HISTORY_VIEW);

        data = service.compress(data, maxResults);
        Map<String, Object> model = new HashMap<>(data.asModel());
        model.put("client", client);
        model.put("custom", true);
        model.put("from", from);
        model.put("to", to);
        model.put("maxResults", maxResults);
        model.put("dataTypes", dataTypes.values());
        return new ModelAndView(String.format("charts/%s", dataType.getPrefix()), model, HttpStatus.OK);
    }
}
