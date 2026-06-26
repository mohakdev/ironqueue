package com.ironqueue.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PdfRest {
    public static String retrieveResource(String resId, String apiKey) throws Exception {
        StringBuilder sb = new StringBuilder();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("https://api.pdfrest.com/resource/" + resId + "?format=url");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Api-Key", apiKey)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        sb.append("File Uploaded Successfully\n");
        sb.append("url: ").append(response.body());
        return sb.toString();
    }
}
