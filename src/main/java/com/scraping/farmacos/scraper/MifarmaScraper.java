package com.scraping.farmacos.scraper;

import java.time.Duration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scraping.farmacos.model.Producto;

import lombok.extern.slf4j.Slf4j;

@Component("MifarmaScraper")
@Slf4j
public class MifarmaScraper implements ScraperStrategy {
    @Autowired
    private ChromeOptions options;

    public Producto buscar(String url) {
        log.info("Iniciando scraping para Mifarma con URL: {}", url);

        WebDriver driver = new ChromeDriver(options);

        try {
            // Navegar a la URL
            driver.get(url);

            // Esperar a que la página cargue completamente
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            Producto producto = new Producto();

            // Extraer el nombre del producto
            WebElement nombreElement = driver.findElement(By.cssSelector("h1.product-detail-information__name"));
            if (nombreElement != null) {
                producto.setNombre(nombreElement.getText());
            } else {
                log.warn("No se encontró el nombre del producto en: {}", url);
                return null;
            }

            // Extraer el precio del producto
            WebElement precioElement = driver.findElement(By.cssSelector("div.price-amount"));
            if (precioElement != null) {
                String precioTexto = precioElement.getText().replace("S/", "").trim();
                producto.setPrecio(precioTexto);
            } else {
                log.warn("No se encontró el precio del producto en: {}", url);
            }

            // Extraer el laboratorio (marca)
            WebElement laboratorioElement = driver.findElement(By.cssSelector("section[data-product-brand]"));
            if (laboratorioElement != null) {
                producto.setLaboratorio(laboratorioElement.getAttribute("data-product-brand"));
            } else {
                log.warn("No se encontró el laboratorio del producto en: {}", url);
            }

            // Código Digemid no está disponible directamente
            producto.setCodigoDigemid("No encontrado");
            producto.setFuente("Mifarma: " + url);

            log.info("Producto encontrado Mifarma: {}", producto.toString());
            return producto;

        } catch (Exception e) {
            log.error("Error al extraer datos del producto de Mifarma: ", e);
            return null;
        } finally {
            // Cerrar el navegador
            driver.quit();
        }
    }

}
