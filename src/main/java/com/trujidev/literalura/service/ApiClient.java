package com.trujidev.literalura.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
  private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
  private final HttpClient httpClient;

  public ApiClient() {
    this.httpClient = HttpClient.newHttpClient();
  }

  public String fetchData(String url) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      return response.body();
    } catch (Exception e) {
      logger.error("Ocurrió un error al consumir la API: {}", e.getMessage(), e);
    }
    return null;
  }
}
