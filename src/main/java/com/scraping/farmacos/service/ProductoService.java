package com.scraping.farmacos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scraping.farmacos.persistence.model.Producto;

import jakarta.annotation.PostConstruct;

@Service
public class ProductoService {
    @Autowired
    private ScraperService scraperService;
    private List<Producto> productos;

    public ProductoService() {

    }

    @PostConstruct
    public void initialize() {
        productos = scraperService.buscaProductos(new String[] {
                "TAMSULOSINA 0.4MG X 50 CAPS",
                "APRONAX TABX550MGX120",
                "FRUTTIFLEX-50 ADULTO",
                "CIPROFLOXACINO TABX500MGX100",
                "PARACETAMOL TABX500MGX100"
        });
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
