package com.scraping.farmacos.scraper;

import org.springframework.stereotype.Component;

import com.scraping.farmacos.model.Producto;

@Component
public class InkafarmaScraper {

    public Producto buscar(String nombre){
        Producto producto = new Producto();
        producto.setNombre(nombre);

        
    }

}
