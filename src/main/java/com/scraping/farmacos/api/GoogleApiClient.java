package com.scraping.farmacos.api;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GoogleApiClient {

    private final HttpClient client = HttpClient.newHttpClient();
    @Value("${google.api.key}")
    private String apiKey;
    private final ObjectMapper mapper = new ObjectMapper();

    public List<GoogleApiModel> getResult(String query) {
        query = URLEncoder.encode(query, StandardCharsets.UTF_8);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://google.serper.dev/search?q=" + query
                            + "&location=Lima+Region%2C+Peru&gl=pe&hl=es-419&apiKey=" + apiKey))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("Error de cliente " + response.statusCode());
            }
            JsonNode organic = mapper.readTree(response.body()).get("organic");

            List<GoogleApiModel> results = mapper.convertValue(organic, new TypeReference<List<GoogleApiModel>>() {
            });
            return results;

        } catch (Exception e) {
            log.error("Error al realizar la solicitud a la API de Google: ", e);
            return new ArrayList<>();
        }

    }

}
