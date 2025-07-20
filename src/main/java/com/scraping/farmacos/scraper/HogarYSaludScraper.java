package com.scraping.farmacos.scraper;

import java.net.URL;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scraping.farmacos.model.Producto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HogarYSaludScraper {

    private final ObjectMapper mapper = new ObjectMapper();

    public Producto buscar(String nombreProducto){
        try{
            Document doc = Jsoup.connect("https://www.hogarysalud.com.pe/?s="+URLEncoder.encode(nombreProducto,"UTF-8")+"&post_type=product")
                    .userAgent("Mozilla")
                    .get();
            String productoWeb = doc.selectFirst("span.gtm4wp_productdata").attributes().get("data-gtm4wp_product_data").toString();
            JsonNode productoJson = mapper.readTree(productoWeb);
            Producto producto = new Producto();
            producto.setNombre(productoJson.get("item_name").asText());
            producto.setPrecio(productoJson.get("price").asText());
            producto.setLaboratorio(productoJson.get("item_category3").asText());
            producto.setCodigoDigemid(productoJson.get("sku").asText());
            producto.setFuente("Hogar y Salud: " + productoJson.get("productlink").asText());
            log.info("Producto encontrado HOGAR Y SALUD: {}", producto.toString());
            return producto;
            
        }catch(Exception e){
            log.error("Error al extraer datos del producto: ", e);
            return null;
        }
        

    }
}
