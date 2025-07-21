package com.scraping.farmacos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scraping.farmacos.api.GoogleApiClient;
import com.scraping.farmacos.scraper.BoticasYSaludScraper;
import com.scraping.farmacos.scraper.HogarYSaludScraper;
import com.scraping.farmacos.scraper.InkafarmaScraper;
import com.scraping.farmacos.scraper.MifarmaScraper;


@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private InkafarmaScraper inkafarmaScraper;
    @Autowired
    private MifarmaScraper mifarmaScraper;
    @Autowired
    private HogarYSaludScraper hogarYSaludScraper;
    @Autowired
    private BoticasYSaludScraper boticasYSaludScraper;

    @Autowired
    private GoogleApiClient googleApiClient;


    @GetMapping
    public String index() {
        //inkafarmaScraper.buscar("TAMSULOSINA 0.4MG X 50CAPS");
        //mifarmaScraper.buscar("TAMSULOSINA 0.4MG X 50CAPS");
        //hogarYSaludScraper.buscar("TAMSULOSINA 0.4MG X 50CAPS");
        //boticasYSaludScraper.buscar("PARACETAMOL");
        googleApiClient.getResult("PARACETAMOL TABX500MGX100");
        
        return "home/index";
    }
    
}
