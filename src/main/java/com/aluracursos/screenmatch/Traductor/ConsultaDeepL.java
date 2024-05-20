package com.aluracursos.screenmatch.Traductor;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ConsultaDeepL {
    private static final String API_KEY = "f83921a9-3e9a-4db2-8350-c802e9006703:fx";
    private static final String API_URL = "https://api-free.deepl.com/v2/translate";

    public static String obtenerTraduccion(String texto) {
        try {
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
                String responseBody = response.body();
                int start = responseBody.indexOf("\"text\":\"") + 8;
                int end = responseBody.indexOf("\"", start);
                return responseBody.substring(start, end);
            } else {
                throw new RuntimeException("Error en la traducción: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al enviar la solicitud HTTP: " + e.getMessage());
        }
    }
}
