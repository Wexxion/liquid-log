package ru.naumen.perfhouse.controllers;

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
import java.text.ParseException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            this.defaultDataType = dataTypes.stream().min(Comparator.comparing(IDataType::getPrefix)).get();
    }

    @RequestMapping(path = "/history/{client}/{year}/{month}/{day}")
    public ModelAndView indexByDay(@PathVariable("client") String client,
            @PathVariable(name = "year", required = false) int year,
            @PathVariable(name = "month", required = false) int month,
            @PathVariable(name = "day", required = false) int day) throws ParseException
    {
        return getDataAndViewByDate(client, defaultDataType, year, month, day, false);
    }

    @RequestMapping(path = "/history/{client}/{type}/{year}/{month}/{day}")
    public ModelAndView customByDay(@PathVariable("client") String client,
                                    @PathVariable("type") String type,
                                    @PathVariable(name = "year", required = false) int year,
                                    @PathVariable(name = "month", required = false) int month,
                                    @PathVariable(name = "day", required = false) int day) throws ParseException
    {
        return getDataAndViewByDate(client, dataTypes.getOrDefault(type, null), year, month, day, false);
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
        model.put("dataTypes", dataTypes.values().stream().map(IDataType::getPrefix).filter(x -> !x.equals(type.getPrefix())).collect(Collectors.toList()));

        return new ModelAndView(String.format("charts/%s", type.getPrefix()), model, HttpStatus.OK);
    }

    @RequestMapping(path = "/history/{client}/custom/{type}")
    public ModelAndView customIndex(@PathVariable("client") String client,
                                    @PathVariable("type") String type,
                                    @RequestParam("from") String from,
                                    @RequestParam("to") String to,
                                    @RequestParam("maxResults") int maxResults) throws ParseException
    {
        return getDataAndViewCustom(client, dataTypes.getOrDefault(type, null), from, to, maxResults);
    }

    @RequestMapping(path = "/history/{client}/custom")
    public ModelAndView customDefaultIndex(@PathVariable("client") String client,
                                    @RequestParam("from") String from,
                                    @RequestParam("to") String to,
                                    @RequestParam("maxResults") int maxResults) throws ParseException
    {
        return getDataAndViewCustom(client, defaultDataType, from, to, maxResults);
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
        model.put("dataTypes", dataTypes.values().stream().map(IDataType::getPrefix).filter(x -> !x.equals(dataType.getPrefix())).collect(Collectors.toList()));

        return new ModelAndView(String.format("charts/%s", dataType.getPrefix()), model, HttpStatus.OK);
    }

    @RequestMapping(path = "/history/{client}")
    public ModelAndView indexLast864(@PathVariable("client") String client,
                                     @RequestParam(name = "count", defaultValue = "864") int count) throws ParseException
    {
        return getDataAndViewLast(client, defaultDataType, count);
    }

    @RequestMapping(path = "/history/{client}/{type}")
    public ModelAndView customLast864(@PathVariable("client") String client,
                                      @PathVariable("type") String type,
                                      @RequestParam(name = "count", defaultValue = "864") int count) throws ParseException
    {
        return getDataAndViewLast(client, dataTypes.getOrDefault(type, null), count);
    }

    private ModelAndView getDataAndViewLast(String client, IDataType dataType, int count) throws ParseException{
        if(dataType == null) return new ModelAndView(NO_HISTORY_VIEW);
        ru.naumen.perfhouse.statdata.StatData d = service.getData(client, defaultDataType, count);
        if (d == null) return new ModelAndView(NO_HISTORY_VIEW);

        Map<String, Object> model = new HashMap<>(d.asModel());
        model.put("client", client);
        model.put("dataTypes", dataTypes.values().stream().map(IDataType::getPrefix).filter(x -> !x.equals(defaultDataType.getPrefix())).collect(Collectors.toList()));

        return new ModelAndView(String.format("charts/%s", defaultDataType.getPrefix()), model, HttpStatus.OK);
    }
}
