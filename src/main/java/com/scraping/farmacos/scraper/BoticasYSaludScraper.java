package com.scraping.farmacos.scraper;

import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.scraping.farmacos.model.Producto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BoticasYSaludScraper {

    public Producto buscar(String nombreProducto){
        try{
            Document doc = Jsoup.connect("https://www.boticasysalud.com/tienda/busqueda?q="+URLEncoder.encode(nombreProducto,"UTF-8"))
                    .userAgent("Mozilla")
                    .get();
            Element productoWeb = doc.selectFirst("div.products-list__item");
            Producto producto = new Producto();
            producto.setNombre(productoWeb.selectFirst("div.product-card__name").text());
            producto.setPrecio(productoWeb.selectFirst("div.product__summary-new-price").text());
            producto.setLaboratorio(productoWeb.selectFirst("div.product-card__brand-content .product-card__brand").text());
            producto.setCodigoDigemid(productoWeb.selectFirst("div.product-card__name").text()==null? "": productoWeb.selectFirst("div.product-card__name").text());
            producto.setFuente("Hogar y Salud: " + productoWeb.selectFirst(".attr(\"href\")").text());
            log.info("Producto encontrado BOTICAS : {}", producto.toString());
            return producto;
            
        }catch(Exception e){
            log.error("Error al extraer datos del producto: ", e);
            return null;
        }
        

    }

}
