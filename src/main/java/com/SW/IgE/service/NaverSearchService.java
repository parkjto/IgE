package com.SW.IgE.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NaverSearchService {

    @Value("${naver.client-id}")
    private String NAVER_API_ID;

    @Value("${naver.secret}")
    private String NAVER_API_SECRET;

    public List<Map<String, String>> searchRestaurants(String query) {
        List<Map<String, String>> restaurants = new ArrayList<>();
        try {
            URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com/v1/search/local")
                    .queryParam("query", query)
                    .queryParam("display", 10)
                    .queryParam("start", 1)
                    .queryParam("sort", "random")
                    .encode()
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", NAVER_API_ID);
            headers.set("X-Naver-Client-Secret", NAVER_API_SECRET);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String body = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(body);
                JsonNode itemsNode = rootNode.path("items");

                for (JsonNode itemNode : itemsNode) {
                    Map<String, String> restaurant = new HashMap<>();
                    restaurant.put("title", itemNode.path("title").asText());
                    restaurant.put("address", itemNode.path("address").asText());
                    restaurant.put("latitude", itemNode.path("mapy").asText());
                    restaurant.put("longitude", itemNode.path("mapx").asText());
                    restaurants.add(restaurant);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restaurants;
    }
}

