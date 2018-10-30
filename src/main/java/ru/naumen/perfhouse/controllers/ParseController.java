package ru.naumen.perfhouse.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.LogParserMain;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Controller
public class ParseController {
    private Logger Log = LoggerFactory.getLogger(ParseController.class);
    private InfluxDAO influxDAO;

    @Inject
    public ParseController(InfluxDAO influxDAO) {
        this.influxDAO = influxDAO;
    }

    @RequestMapping(path = "/parse")
    public ModelAndView index() {
        return new ModelAndView("parse", new HashMap<>(), HttpStatus.OK);
    }

    @RequestMapping(path = "/parse/upload", method = RequestMethod.POST)
    public String upload(
            @RequestParam("dbName") String dbName,
            @RequestParam("parseMode") String parseMode,
            @RequestParam("file") MultipartFile file,
            @RequestParam("timeZone") String timeZone,
            @RequestParam(value = "logTrace", required = false) String logTrace) {
        try {
            Path filepath = Paths.get(System.getProperty("user.dir"), "download", file.getOriginalFilename());
            new File(filepath.getParent().toString()).mkdirs();
            if(!Files.exists(filepath))
                Files.copy(file.getInputStream(), filepath);

            LogParserMain.Parse(influxDAO, dbName, parseMode, filepath, timeZone, logTrace == null);
        } catch (Exception ex) {
            Log.error(ex.toString(), ex);
        }

        return "redirect:/parse/result";
    }

    @RequestMapping(path = "/parse/result", method = RequestMethod.GET)
    public ModelAndView parseResult(RedirectAttributes redirectAttributes) {
        return new ModelAndView("parse_result", new HashMap<>(), HttpStatus.OK);
    }
}
