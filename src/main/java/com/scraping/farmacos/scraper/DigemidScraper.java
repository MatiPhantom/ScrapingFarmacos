package com.scraping.farmacos.scraper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DigemidScraper {

    @Autowired
    private ChromeOptions chromeOptions;

    private final Random random = new Random();

    public String buscarCodigoSanitario(String query) {

        WebDriver driver = new ChromeDriver(chromeOptions);

        try {

            // Abrir la página de consulta de productos
            driver.get("https://opm-digemid.minsa.gob.pe/#/consulta-producto");

            // Configurando tiempo maximo de espera
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(90));

            // 1er modal
            By closeModalButtonSelector = By.xpath("//ngb-modal-window//button[contains(text(), 'Cerrar')]");
            WebElement closeModalButton = wait.until(ExpectedConditions.elementToBeClickable(closeModalButtonSelector));
            closeModalButton.click();
            esperarUnMomento(1000);

            // 2do modal
            By secondModalCloseButtonSelector = By.xpath("//ngb-modal-window//button[contains(text(), 'Cerrar')]");
            WebElement secondModalCloseButton = wait
                    .until(ExpectedConditions.elementToBeClickable(secondModalCloseButtonSelector));
            secondModalCloseButton.click();
            esperarUnMomento(1000);

            // Insertando query en Input DIGEMID
            WebElement input = wait.until(ExpectedConditions
                    .elementToBeClickable(By.cssSelector("ng-autocomplete[name='nombreProducto'] input")));
            input.click();
            input.clear();
            input.sendKeys(query);
            if (random.nextInt(10) < 3) {
                input.sendKeys(" ");
                Thread.sleep(300);
                input.sendKeys("\b"); // Simula borrar espacio
            }
            esperarUnMomento(1500);

            // Seleccionando autocompletado
            int attempts = 0;
            boolean clicked = false;
            while (attempts < 3 && !clicked) {
                try {
                    // Volvemos a buscar el elemento DENTRO del bucle para obtener una referencia
                    // "fresca"
                    By firstSuggestionSelector = By
                            .cssSelector("div.suggestions-container.is-visible li.item:first-child a");
                    WebElement firstSuggestion = wait
                            .until(ExpectedConditions.elementToBeClickable(firstSuggestionSelector));
                    firstSuggestion.click();

                    esperarUnMomento(1500);
                    clicked = true; // Si el clic fue exitoso, salimos del bucle

                } catch (StaleElementReferenceException e) {
                    attempts++;
                }
            }

            // Presionando Boton Buscar
            By searchButtonSelector = By.xpath("//button[contains(., 'Buscar')]");
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(searchButtonSelector));
            searchButton.click();
            esperarUnMomento(2000);

            // Esperar a que se cargue la tabla de resultados
            WebElement firstResultTable = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.cssSelector("table.table.table-striped tbody tr"))).get(0);

            // Presionando en los detalles
            WebElement celdaDetalles = firstResultTable.findElements(By.tagName("td")).get(6);
            WebElement detalleLink = celdaDetalles.findElement(By.cssSelector("a[title='Ver detalle']"));
            detalleLink.click();

            WebElement modal = wait
                    .until(ExpectedConditions
                            .visibilityOfElementLocated(By.cssSelector("ngb-modal-window app-modal-producto-detalle")));
            System.out.println("Modal de detalles de producto abierto.");
            WebElement registroSanitarioInput = wait.until(drivers -> {
                WebElement inputReg = modal.findElement(By.cssSelector("input[name='registroSanitario']"));
                String value = inputReg.getAttribute("ng-reflect-model");
                return (value != null && !value.isEmpty()) ? inputReg : null;
            });
            String registroSanitario = registroSanitarioInput.getAttribute("ng-reflect-model");
            log.info("Registro Sanitario encontrado: " + registroSanitario);
            driver.quit();
            return registroSanitario;

        } catch (Exception e) {
            log.error("Error al extraer datos del producto: " + e.toString());
            driver.quit();
            return "No encontrado";
        }

    }

    private void esperarUnMomento(int baseMilis) {
        try {
            Thread.sleep(baseMilis + random.nextInt(1000)); // base + [0-1000] ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
