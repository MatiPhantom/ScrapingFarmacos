package com.scraping.farmacos.api;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GoogleApiClient {

    private final String API_KEY="328d561b5b8e7001f918ba12b7dfb6a702a151a7";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper= new ObjectMapper();

    public String getResult(String query){
        query=URLEncoder.encode(query, StandardCharsets.UTF_8);
        try{
            HttpRequest request= HttpRequest.newBuilder()
            .uri(URI.create("https://google.serper.dev/search?q="+query+"&location=Lima+Region%2C+Peru&gl=pe&hl=es-419&apiKey="+API_KEY))
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode()!=200){
            log.error("Error de cliente " + response.statusCode());
            }
            JsonNode jsonResponse = mapper.readTree(response.body());

            String result = jsonResponse.get("organic").findValuesAsText("link").getFirst();
            log.info("Resultado de la API de Google: {}", result);
            return result;

        }catch(Exception e){
            log.error("Error al realizar la solicitud a la API de Google: ", e);
            return null;
        }

    }

}
