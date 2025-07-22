package com.scraping.farmacos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.poi.sl.usermodel.ObjectMetaData.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.scraping.farmacos.api.GoogleApiClient;
import com.scraping.farmacos.api.GoogleApiModel;
import com.scraping.farmacos.model.Producto;
import com.scraping.farmacos.scraper.ScraperStrategy;
import com.scraping.farmacos.utils.Dominio;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScraperService {

    @Autowired
    private GoogleApiClient googleApiClient;

    @Autowired
    private ApplicationContext context;

    public List<GoogleApiModel> buscarProducto(String query) {
        return googleApiClient.getResult(query);
    }

    public ScraperStrategy evaluarScraper(String url) {
        String dominio = Dominio.getDominio(url);
        return switch (dominio) {
            case "inkafarma.pe" -> context.getBean("InkafarmaScraper", ScraperStrategy.class);
            case "mifarma.com.pe" -> context.getBean("MifarmaScraper", ScraperStrategy.class);
            case "hogarysalud.com.pe" -> context.getBean("HogarYSaludScraper", ScraperStrategy.class);
            case "pjfarma.pe" -> context.getBean("PJFarmaScraper", ScraperStrategy.class);
            default -> null;
        };
    }

    public List<Producto> buscaProductos(String[] querys) {
        List<Producto> productos = new ArrayList<>();
        for (String query : querys) {
            List<GoogleApiModel> results = buscarProducto(query);
            results = Dominio.filtrarDominios(results);

            String mejorCoincidencia = Dominio.evaluarSimilitd(results, query);

            ScraperStrategy scraper = evaluarScraper(mejorCoincidencia);
            log.info("Bean seleccionado: " + scraper.getClass().getSimpleName());
            Producto farmaco = scraper.buscar(mejorCoincidencia);
            if (farmaco != null) {
                log.info("Producto encontrado: {}", farmaco);
                productos.add(farmaco);
            } else {
                log.warn("No se encontró información del producto para la URL: {}", mejorCoincidencia);
            }
        }
        return productos.stream().filter(p -> p != null).collect(Collectors.toList());
    }

}
