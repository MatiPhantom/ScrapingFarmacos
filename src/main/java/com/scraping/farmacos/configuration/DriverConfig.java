package com.scraping.farmacos.configuration;

import java.io.File;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DriverConfig {

    // private final String driverPath = new
    // File("src/main/resources/driver/chromedriver.exe").getAbsolutePath();;

    @Bean
    public ChromeOptions chromeOptions() {
        // System.setProperty("webdriver.chrome.driver", driverPath);
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless");
        options.addArguments("start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu"); // Deshabilitar GPU (recomendado para headless)
        // options.setBinary(System.getenv("CHROME_BIN")); //LINEA ADICIONAL PARA RENDER

        return options;
    }

}
