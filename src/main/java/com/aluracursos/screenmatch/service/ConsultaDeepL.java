package com.aluracursos.screenmatch.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ConsultaDeepL {
    private static final String API_KEY = "f83921a9-3e9a-4db2-8350-c802e9006703:fx"; // Reemplaza con tu clave de API de DeepL
    private static final String API_URL = "https://api-free.deepl.com/v2/translate";

    public static String obtenerTraduccion(String texto) throws IOException, InterruptedException {
        String encodedText = URLEncoder.encode(texto, StandardCharsets.UTF_8);
        String url = String.format("%s?auth_key=%s&text=%s&target_lang=ES", API_URL, API_KEY, encodedText);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Procesar la respuesta JSON y extraer la traducción
            String responseBody = response.body();
            // Aquí se asume que la respuesta tiene el formato JSON esperado
            // {"translations":[{"detected_source_language":"EN","text":"texto traducido"}]}
            int start = responseBody.indexOf("\"text\":\"") + 8;
            int end = responseBody.indexOf("\"", start);
            return responseBody.substring(start, end);
        } else {
            throw new RuntimeException("Error en la traducción: " + response.body());
        }
    }
}
