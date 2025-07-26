package com.scraping.farmacos.scraper;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scraping.farmacos.persistence.model.Producto;

import lombok.extern.slf4j.Slf4j;

@Component("InkafarmaScraper")
@Slf4j
public class InkafarmaScraper implements ScraperStrategy {

    @Autowired
    private ChromeOptions options;

    public Producto buscar(String url) {
        log.info("Iniciando scraping para Hogar y Salud con URL: {}", url);
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

            try {
                WebElement digemidElement = driver.findElement(
                        By.xpath("//div[contains(@class, 'description-content')]//li[contains(text(), 'RS:')]"));
                String digemidTexto = digemidElement.getText();
                if (digemidTexto.contains(":")) {
                    producto.setCodigoDigemid(digemidTexto.split(":")[1].trim());
                } else {
                    producto.setCodigoDigemid("No encontrado");
                }
            } catch (NoSuchElementException e) {
                producto.setCodigoDigemid("No encontrado");
            }

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
