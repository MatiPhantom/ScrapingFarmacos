package com.scraping.farmacos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scraping.farmacos.scraper.InkafarmaScraper;
import com.scraping.farmacos.scraper.MifarmaScraper;


@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private InkafarmaScraper inkafarmaScraper;
    @Autowired
    private MifarmaScraper mifarmaScraper;

    @GetMapping
    public String index() {
        inkafarmaScraper.buscar("paracetamol");
        mifarmaScraper.buscar("paracetamol");
        return "home/index";
    }
    
}
