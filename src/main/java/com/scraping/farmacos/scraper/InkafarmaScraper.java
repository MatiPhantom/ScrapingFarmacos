package com.scraping.farmacos.scraper;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scraping.farmacos.model.Producto;

import lombok.extern.slf4j.Slf4j;

@Component("InkafarmaScraper")
@Slf4j
public class InkafarmaScraper implements ScraperStrategy {

    @Autowired
    private ChromeOptions options;

    public Producto buscar(String url) {
        log.info("Iniciando scraping para Inkafarma con URL: {}", url);
        WebDriver driver = new ChromeDriver(options);

        try {
            // Navegar a la URL
            driver.get(url);

            // Esperar a que la página cargue completamente
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

            Producto producto = new Producto();

            // Extraer el nombre del producto
            WebElement nombreElement = driver.findElement(By.cssSelector("h1.product-detail-information__name"));
            if (nombreElement != null) {
                producto.setNombre(nombreElement.getText());
            } else {
                log.warn("No se encontró el nombre del producto en: {}", url);
                return null;
            }

            // Extraer el precio
            WebElement precioElement = driver.findElement(By.cssSelector("div.price-amount"));
            if (precioElement != null) {
                String precioTexto = precioElement.getText().replace("S/", "").trim();
                producto.setPrecio(precioTexto);
            }

            // Extraer el laboratorio
            WebElement laboratorioElement = driver.findElement(By.cssSelector("section[data-product-brand]"));
            if (laboratorioElement != null) {
                producto.setLaboratorio(laboratorioElement.getAttribute("data-product-brand"));
            }

            // Código Digemid no está disponible directamente en esta página
            producto.setCodigoDigemid("No encontrado");
            producto.setFuente("Inkafarma: " + url);

            log.info("Producto encontrado INKFARMA: {}", producto.toString());
            return producto;

        } catch (Exception e) {
            log.error("Error al extraer datos del producto: ", e);
            return null;
        } finally {
            // Cerrar el navegador
            driver.quit();
        }
    }
}
