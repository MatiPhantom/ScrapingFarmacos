package com.scraping.farmacos.scraper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.scraping.farmacos.model.Producto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InkafarmaScraper {

    public void buscar(String nombreProducto){
        Producto producto = new Producto();
        producto.setNombre(nombreProducto);

        String url= "https://inkafarma.pe/buscador?keyword=" + URLEncoder.encode(nombreProducto, StandardCharsets.UTF_8);

        try{
            //Conectando a la URL 
            Document doc = Jsoup.connect(url).get();
            log.info("Conectado a la URL: " + url);
        }catch(Exception e){
            log.error("Conexion fallida a la URL: "+url, e);
        }

    }

}
