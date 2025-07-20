package com.scraping.farmacos.scraper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scraping.farmacos.model.Producto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InkafarmaScraper {

    @Autowired
    private ChromeOptions chromeOptions;

    private final ObjectMapper mapper= new ObjectMapper();
    

    public Producto buscar(String nombreProducto) {

        WebDriver driver = new ChromeDriver(chromeOptions);

        driver.get(
                "https://inkafarma.pe/buscador?keyword=" + URLEncoder.encode(nombreProducto, StandardCharsets.UTF_8));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.card.product")));

        WebElement webProducto = driver.findElement(By.cssSelector("div.card.product"));

        String dataProducto = webProducto.getAttribute("data-product");
        try{
            JsonNode jsonProducto = mapper.readTree(dataProducto);
            Producto producto= new Producto();
            producto.setNombre(jsonProducto.get("name").asText());
            producto.setPrecio(jsonProducto.get("presentations").get(0).get("price").asText());
            producto.setLaboratorio(jsonProducto.get("brand").asText());
            producto.setCodigoDigemid(jsonProducto.get("sapCode").asText());
            String url = webProducto.findElement(By.cssSelector("a.link")).getAttribute("href");

            producto.setFuente("Inkafarma: "+ url);

            log.info("Producto encontrado INKFARMA: {}", producto.toString());

            return producto;
        }catch(Exception e){
            
            log.error("Error al extraer datos del producto: ", e);
            driver.quit();
            return null;
        }
        


    }

}
