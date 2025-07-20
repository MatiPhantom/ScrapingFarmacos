package com.scraping.farmacos.scraper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import com.scraping.farmacos.model.Producto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InkafarmaScraper {

    public void buscar(String nombreProducto) {

        // 1. Adicionando propiedad del driver de Chrome
        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver.exe");

        // 2. Usa Brave como navegador
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe"); // Ajusta si está
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // en otro lugar

        // 3. Inicia el navegador controlado por Selenium
        WebDriver driver = new ChromeDriver(options);

        // 4. Abirendo conexión con la URL
        driver.get("https://inkafarma.pe/buscador?keyword=paracetamol");

        // 5. Determinando tiempo de espera maximo para encontrar los elementos
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            // 6. Configurando selector para encontrar los contenedores de productos
            String selectorContenedor = "fp-filtered-product-list div.col-12[class*='col-sm-6']";
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(selectorContenedor)));
            List<WebElement> contenedoresDeProductos = driver.findElements(By.cssSelector(selectorContenedor));
            System.out
                    .println("Se encontraron " + contenedoresDeProductos.size() + " contenedores de productos únicos.");

            for (WebElement contenedor : contenedoresDeProductos) {
                // Dentro de cada contenedor, busca la información del producto.
                // No importa si encuentra la versión grande o pequeña, los datos son los
                // mismos.
                WebElement tarjetaProducto = contenedor.findElement(By.cssSelector("div.card.product"));

                String nombre = tarjetaProducto.getAttribute("data-product-name");
                String marca = tarjetaProducto.getAttribute("data-product-brand");

                String precio = "";
                String url = "";

                try {
                    WebElement precioEl = tarjetaProducto.findElement(By.cssSelector("span.card-monedero span"));
                    precio = precioEl.getText();
                } catch (Exception e) {
                    precio = "No disponible";
                }

                try {
                    WebElement enlace = tarjetaProducto.findElement(By.cssSelector("a.link"));
                    url = enlace.getAttribute("href");
                } catch (Exception e) {
                    url = "Sin URL";
                }

                System.out.println("Nombre: " + nombre);
                System.out.println("Marca: " + marca);
                System.out.println("Precio: " + precio);
                System.out.println("URL: " + url);
                System.out.println("---------");
            }

        } catch (Exception e) {
            // 6. Cierra el navegador
            driver.quit();
            log.error("Error al buscar productos: ", e);
        }

    }

}
