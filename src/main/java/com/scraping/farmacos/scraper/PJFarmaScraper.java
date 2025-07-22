package com.scraping.farmacos.scraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scraping.farmacos.model.Producto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component("PJFarmaScraper")
@Slf4j
public class PJFarmaScraper implements ScraperStrategy {

    private final ObjectMapper mapper = new ObjectMapper();

    public Producto buscar(String url) {
        log.info("Iniciando scraping para PJ Farma con URL: {}", url);
        try {
            // 1. Conectar y obtener el HTML con Jsoup
            Document doc = Jsoup.connect(url)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            // 2. Seleccionar todos los scripts JSON-LD y encontrar el del producto
            Elements scriptElements = doc.select("script[type=application/ld+json]");
            JsonNode jsonProducto = null;

            for (Element scriptElement : scriptElements) {
                String jsonData = scriptElement.data();
                // Buscamos el script que define un objeto de tipo "Product"
                if (jsonData.contains("\"@type\":\"Product\"")) {
                    jsonProducto = mapper.readTree(jsonData);
                    break;
                }
            }

            if (jsonProducto == null) {
                log.error("No se encontró el script JSON-LD de tipo 'Product' en la página: {}", url);
                return null;
            }

            // 3. Mapear los datos al objeto Producto
            Producto producto = new Producto();
            producto.setNombre(jsonProducto.path("name").asText().trim());
            producto.setPrecio(jsonProducto.path("offers").path("price").asText());
            producto.setLaboratorio(jsonProducto.path("brand").path("name").asText());
            producto.setCodigoDigemid("No encontrado");
            producto.setFuente("PJ Farma: " + url);

            log.info("Producto encontrado PJ Farma: {}", producto.toString());
            return producto;
        } catch (Exception e) {
            log.error("Error al extraer datos del producto de PJ Farma: ", e);
            return null;
        }
    }
}