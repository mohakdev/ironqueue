package com.ironqueue.executor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.cert.X509Certificate;
import java.time.Duration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.ironqueue.job.Job;
import com.ironqueue.util.Logger;

public class QuoteJobExecutor implements JobExecutor {
    @Override
    public void execute(Job job) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI("https://api.quotable.io/quotes/random"))
            .GET()
            .build();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] {
            new javax.net.ssl.X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            }
        }, new java.security.SecureRandom());

        HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .sslContext(sslContext)
            .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String output = response.body();
        System.out.println(output);
        Logger.Log(getClass(),"Quote retrieved from API");
    }
}
