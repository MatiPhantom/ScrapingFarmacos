package com.scraping.farmacos.configuration;

import java.io.File;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DriverConfig {

    private final String driverPath = new File("src/main/resources/driver/chromedriver.exe").getAbsolutePath();;

    @Bean
    public ChromeOptions chromeOptions() {
        System.setProperty("webdriver.chrome.driver", driverPath);
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless");
        options.addArguments("start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu"); // Deshabilitar GPU (recomendado para headless)
        options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
        options.addArguments(
                "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");
        options.addArguments("--disable-extensions"); // Deshabilitar extensiones del navegador
        options.addArguments("--remote-allow-origins=*"); // Permitir todas las conexiones remotas (opcional)
        options.setExperimentalOption("useAutomationExtension", false);

        return options;
    }

}
