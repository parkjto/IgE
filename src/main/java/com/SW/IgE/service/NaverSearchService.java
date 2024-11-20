package com.SW.IgE.service;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class NaverSearchService {

    private static final Logger LOGGER = Logger.getLogger(NaverSearchService.class.getName());

    @Value("${naver.client-id}")
    private String NAVER_API_ID;

    @Value("${naver.secret}")
    private String NAVER_API_SECRET;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 네이버 검색 API를 이용하여 장소를 검색하는 메서드입니다.
     *
     * @param query 검색할 장소의 이름 또는 검색어
     * @return 검색된 장소 목록
     */
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

                    // 좌표 값 처리
                    try {
                        String mapyRaw = itemNode.path("mapy").asText();
                        String mapxRaw = itemNode.path("mapx").asText();

                        LOGGER.log(Level.INFO, "좌표 원본 데이터 - mapy: {0}, mapx: {1}", new Object[]{mapyRaw, mapxRaw});

                        double latitude = Double.parseDouble(mapyRaw);
                        double longitude = Double.parseDouble(mapxRaw);

                        restaurant.put("latitude", String.valueOf(latitude));
                        restaurant.put("longitude", String.valueOf(longitude));
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.SEVERE, "좌표 변환 실패: {0}", e.getMessage());
                        restaurant.put("latitude", "null");
                        restaurant.put("longitude", "null");
                    }

                    restaurants.add(restaurant);
                }
                LOGGER.log(Level.INFO, "총 검색된 식당 수: {0}", restaurants.size());
            } else {
                LOGGER.log(Level.WARNING, "API 호출 실패, 응답 코드: {0}", response.getStatusCode());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "네이버 API 호출 중 오류 발생", e);
        }

        return restaurants;
    }

}
