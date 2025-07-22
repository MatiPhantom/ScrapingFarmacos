package com.scraping.farmacos.scraper;

import com.scraping.farmacos.model.Producto;

public interface ScraperStrategy {

    Producto buscar(String url);

}
