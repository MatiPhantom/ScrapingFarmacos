package com.scraping.farmacos.scraper;

import java.net.URL;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scraping.farmacos.persistence.model.Producto;

import lombok.extern.slf4j.Slf4j;

@Component("HogarYSaludScraper")
@Slf4j
public class HogarYSaludScraper implements ScraperStrategy {

    private final ObjectMapper mapper = new ObjectMapper();

    public Producto buscar(String url) {
        log.info("Iniciando scraping para Hogar y Salud con URL: {}", url);
        try {
            // 1. Conectar y obtener el HTML con Jsoup
            Document doc = Jsoup.connect(url)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            // 2. La mejor fuente de datos es un input oculto para Google Tag Manager
            Element gtmDataElement = doc.selectFirst("input[name=gtm4wp_product_data]");

            if (gtmDataElement == null) {
                log.error("No se encontró el input 'gtm4wp_product_data' en la página: {}", url);
                return null;
            }

            // 3. Extraer y parsear el JSON del atributo 'value'
            String jsonData = gtmDataElement.attr("value");
            JsonNode jsonProducto = mapper.readTree(jsonData);

            // 4. Mapear los datos al objeto Producto
            Producto producto = new Producto();
            producto.setNombre(jsonProducto.path("item_name").asText().trim());
            producto.setPrecio(jsonProducto.path("price").asText());
            producto.setLaboratorio(jsonProducto.path("item_brand").asText());
            producto.setCodigoDigemid("No encontrado");
            producto.setFuente("Hogar y Salud: " + url);

            log.info("Producto encontrado Hogar y Salud: {}", producto.toString());
            return producto;

        } catch (Exception e) {
            log.error("Error al extraer datos del producto de Hogar y Salud: ", e);
            return null;
        }

    }
}
