package org.yalli.wah.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.yalli.wah.model.dto.CoordinateDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;


public class CoordinateUtil {
    public static CoordinateDto findCoordinate(String searchCity) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        searchCity = TranslateUtil.getCityTranslation(searchCity);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://secure.geonames.org/searchJSON?q=" + searchCity + "&maxRows=1&username=dedatom596minduls.c"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.body());
        Float lng = 0f;
        Float lat = 0f;
        for (JsonNode node : rootNode.path("geonames")) {
            System.out.println(node.path("lng").asText());
            System.out.println(node.path("lat").asText());
            System.out.println(Float.parseFloat(node.path("lng").textValue()));
            lng = Float.parseFloat(node.path("lng").textValue());
            lat = Float.parseFloat(node.path("lat").textValue());
        }

        return new CoordinateDto(lng, lat);
    }

    public static Map<String, String> findToponymByCoordinate(CoordinateDto coordinateDto) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://secure.geonames.org/findNearbyPlaceNameJSON?lat=" + coordinateDto.getLat() + "&lng=" + coordinateDto.getLng()+"&username=dedatom596minduls.c"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.body());
        String city = null;
        String country = null;
        for (JsonNode node : rootNode.path("geonames")) {
            city = node.path("toponymName").textValue();
            country = node.path("countryName").textValue();
        }
        country = TranslateUtil.getAzerbaijani(country);
        city = TranslateUtil.getCityInAzerbajani(city);
        return  Map.of(country, city);
    }
}
