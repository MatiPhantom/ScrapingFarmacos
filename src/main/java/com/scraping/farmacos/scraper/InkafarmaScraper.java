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

        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver.exe");

        // 2. Usa Brave como navegador
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe"); // Ajusta si está
                                                                                                      // en otro lugar

        // 3. Inicia el navegador controlado por Selenium
        WebDriver driver = new ChromeDriver(options);

        List<WebElement> productos;
        // 4. Abre la URL
        driver.get("https://inkafarma.pe/buscador?keyword=paracetamol");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // 5. Espera unos segundos para ver resultados
        try {

            // Buscar los productos en la página
            // List<WebElement> productos =
            // driver.findElements(By.cssSelector("div.card.product"));
            productos = wait
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.card.product")));

            if (productos.isEmpty()) {
                System.out.println("No se encontraron productos, incluso después de esperar.");
            } else {
                System.out.println("¡Éxito! Se encontraron " + productos.size() + " productos.");
            }

            for (WebElement productoElemento : productos) {
                String nombre = productoElemento.getAttribute("data-product-name");
                String marca = productoElemento.getAttribute("data-product-brand");

                String precio = "";
                String url = "";

                try {
                    WebElement precioEl = productoElemento.findElement(By.cssSelector("span.card-monedero span"));
                    precio = precioEl.getText();
                } catch (Exception e) {
                    precio = "No disponible";
                }

                try {
                    WebElement enlace = productoElemento.findElement(By.cssSelector("a.link"));
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
        }

        /*
         * String url = "https://inkafarma.pe/buscador?keyword="
         * + URLEncoder.encode(nombreProducto, StandardCharsets.UTF_8);
         */
        /*
         * WebDriver driver = new ChromeDriver();
         * try {
         * String url = "https://inkafarma.pe/buscador?keyword="
         * + URLEncoder.encode(nombreProducto, StandardCharsets.UTF_8);
         * driver.get(url);
         * Thread.sleep(5000); // Espera a que cargue JS (ajusta según tu red)
         * 
         * List<WebElement> productos =
         * driver.findElements(By.cssSelector("div.card.product"));
         * System.out.println("Productos encontrados: " + productos.size());
         * for (WebElement producto : productos) {
         * String nombre = producto.getAttribute("data-product-name");
         * String marca = producto.getAttribute("data-product-brand");
         * String precioRegular = "";
         * WebElement precioElem =
         * producto.findElement(By.cssSelector("p.label--2.mb-0.mf.text--1"));
         * if (precioElem != null)
         * precioRegular = precioElem.getText();
         * String precioOferta = "";
         * WebElement ofertaElem =
         * producto.findElement(By.cssSelector("span.card-monedero"));
         * if (ofertaElem != null)
         * precioOferta = ofertaElem.getText();
         * String urlProduct = "https://inkafarma.pe"
         * + producto.findElement(By.cssSelector("a.link")).getAttribute("href");
         * 
         * System.out.println("Nombre: " + nombre);
         * System.out.println("Marca: " + marca);
         * System.out.println("Precio regular: " + precioRegular);
         * System.out.println("Precio oferta: " + precioOferta);
         * System.out.println("URL: " + urlProduct);
         * System.out.println("--------");
         * }
         * } catch (Exception e) {
         * e.printStackTrace();
         * } finally {
         * driver.quit();
         * }
         */

        /*
         * try {
         * // Conectando a la URL y obteniendo documentación
         * Document doc = Jsoup.connect(url).userAgent(
         * "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
         * )
         * .timeout(10000)
         * .get();
         * log.info("Conectado a la URL: " + url + " con éxito " + doc.title());
         * 
         * 
         * Elements nombre= doc.select("data-product-name");
         * Elements precio= doc.select("span.text-strike--1");
         * Elements precioOferta= doc.select("p.label-black--1");
         * 
         * List<Producto> listaProductos= new ArrayList<>();
         * 
         * for(int i=0; i< nombre.size();i++){
         * String titulo= nombre.get(i).text();
         * String precioNormal= precio.get(i).text();
         * String precioDescuento= precioOferta.get(i).text();
         * String link= nombre.get(i).attr("href");
         * 
         * producto.setNombre(titulo);
         * producto.setPrecio(precioNormal);
         * producto.setFuente(link);
         * 
         * log.info("Producto encontrado: " + producto.toString());
         * }
         * 
         * 
         * Elements productos = doc.select("div.card.product");
         * log.info("HTML XD: " + doc.html());
         * log.info("Productos encontrados: " + productos.size());
         * for (Element producto : productos) {
         * String nombre = producto.attr("data-product-name");
         * String marca = producto.attr("data-product-brand");
         * String precioRegular = producto.selectFirst("p.label--2.mb-0.mf.text--1") !=
         * null
         * ? producto.selectFirst("p.label--2.mb-0.mf.text--1").text()
         * : "";
         * String precioOferta = producto.selectFirst("span.card-monedero") != null
         * ? producto.selectFirst("span.card-monedero").text()
         * : "";
         * String urlProduct = "https://inkafarma.pe" +
         * producto.selectFirst("a.link").attr("href");
         * 
         * System.out.println("Nombre: " + nombre);
         * System.out.println("Marca: " + marca);
         * System.out.println("Precio regular: " + precioRegular);
         * System.out.println("Precio oferta: " + precioOferta);
         * System.out.println("URL: " + urlProduct);
         * System.out.println("--------");
         * }
         * 
         * } catch (Exception e) {
         * log.error("Conexion fallida a la URL: " + url, e);
         * }
         */

    }

}
