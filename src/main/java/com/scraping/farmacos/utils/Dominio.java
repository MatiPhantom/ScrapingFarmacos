package com.scraping.farmacos.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.LevenshteinDistance;

import com.scraping.farmacos.api.GoogleApiModel;

public class Dominio {
    public static final List<String> dominios = List.of("inkafarma.pe", "mifarma.com.pe", "boticaysalud.com",
            "hogarysalud.com.pe", "pjfarma.pe");

    public static boolean esDominioValido(String url) {
        return dominios.stream().anyMatch(url::contains);
    }

    public static String getDominio(String url) {
        return dominios.stream()
                .filter(url::contains)
                .findFirst()
                .orElse(null);
    }

    public static List<GoogleApiModel> filtrarDominios(List<GoogleApiModel> results) {
        // Implementar lógica para filtrar dominios específicos
        return results.stream().filter(dom -> esDominioValido(dom.getLink())).collect(Collectors.toList());

    }

    public static String evaluarSimilitd(List<GoogleApiModel> results, String query) {
        // Implementar lógica para evaluar similitud
        LevenshteinDistance distancia = new LevenshteinDistance();
        GoogleApiModel mejorCoincidencia = null;
        int mejorScore = Integer.MAX_VALUE;
        for (GoogleApiModel r : results) {
            String tituloNormalizado = r.getTitle().toLowerCase();
            int score = distancia.apply(query.toLowerCase(), tituloNormalizado);

            if (score < mejorScore) {
                mejorScore = score;
                mejorCoincidencia = r;
            }
        }

        return mejorCoincidencia != null ? mejorCoincidencia.getLink() : null;
    }
}
