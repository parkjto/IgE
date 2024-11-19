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
            // 네이버 검색 API를 호출하기 위한 URI 생성
            URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com/v1/search/local")
                    .queryParam("query", query)
                    .queryParam("display", 10)
                    .queryParam("start", 1)
                    .queryParam("sort", "random")
                    .encode()
                    .build()
                    .toUri();

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", NAVER_API_ID);
            headers.set("X-Naver-Client-Secret", NAVER_API_SECRET);

            // API 호출
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            // 응답이 정상적이면 JSON 처리
            if (response.getStatusCode().is2xxSuccessful()) {
                String body = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(body);
                JsonNode itemsNode = rootNode.path("items");

                for (JsonNode itemNode : itemsNode) {
                    Map<String, String> restaurant = new HashMap<>();
                    restaurant.put("title", itemNode.path("title").asText()); // 장소 이름
                    restaurant.put("address", itemNode.path("address").asText()); // 장소 주소

                    // 위도와 경도를 그대로 저장 (변환 없이 사용)
                    double latitude = Double.parseDouble(itemNode.path("mapy").asText()); // 위도
                    double longitude = Double.parseDouble(itemNode.path("mapx").asText()); // 경도

                    restaurant.put("latitude", String.valueOf(latitude)); // 그대로 저장
                    restaurant.put("longitude", String.valueOf(longitude)); // 그대로 저장

                    // 리스트에 추가
                    restaurants.add(restaurant);
                }

                // 검색된 결과를 로그로 출력
                LOGGER.log(Level.INFO, "검색된 식당 목록: {0}", restaurants);
            } else {
                LOGGER.log(Level.WARNING, "API 호출 실패, 응답 코드: {0}", response.getStatusCode());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "네이버 API 호출 중 오류 발생", e);
        }

        return restaurants;
    }
}
