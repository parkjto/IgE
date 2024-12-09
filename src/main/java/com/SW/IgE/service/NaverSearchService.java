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
import java.util.*;
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

        LOGGER.log(Level.INFO, "searchRestaurants 메서드 호출. 검색어: {0}", query);

        try {
            URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com/v1/search/local")
                    .queryParam("query", query)
                    .queryParam("display", 10)
                    .queryParam("start", 1)
                    .queryParam("sort", "random")
                    .encode()
                    .build()
                    .toUri();

            LOGGER.log(Level.INFO, "네이버 API 요청 URI 생성: {0}", uri);

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", NAVER_API_ID);
            headers.set("X-Naver-Client-Secret", NAVER_API_SECRET);

            LOGGER.log(Level.INFO, "요청 헤더 설정 완료");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.log(Level.INFO, "네이버 API 호출 성공. 응답 코드: {0}", response.getStatusCode());

                String body = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(body);
                JsonNode itemsNode = rootNode.path("items");

                LOGGER.log(Level.INFO, "응답 데이터 파싱 시작");

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
                LOGGER.log(Level.INFO, "응답 데이터 파싱 완료. 총 검색된 식당 수: {0}", restaurants.size());
            } else {
                LOGGER.log(Level.WARNING, "네이버 API 호출 실패, 응답 코드: {0}", response.getStatusCode());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "네이버 API 호출 중 오류 발생", e);
        }

        LOGGER.log(Level.INFO, "searchRestaurants 메서드 종료");
        return restaurants;
    }

    /**
     * 식당 목록에 거리 정보를 추가하는 메서드입니다.
     *
     * @param restaurants 검색된 식당 목록
     * @param userX      사용자 위치의 X 좌표 (경도)
     * @param userY      사용자 위치의 Y 좌표 (위도)
     * @return 거리 정보가 추가된 식당 목록
     */
    public List<Map<String, Object>> searchRestaurantsWithDistance(
            List<Map<String, String>> restaurants,
            double userX,
            double userY,
            double maxDistance) { // 최대 거리 추가

        List<Map<String, Object>> restaurantsWithDistance = new ArrayList<>();

        for (Map<String, String> restaurant : restaurants) {
            double restaurantX = Double.parseDouble(restaurant.get("longitude"));
            double restaurantY = Double.parseDouble(restaurant.get("latitude"));

            double distance = calculateDistance(userY, userX, restaurantY, restaurantX);

            // 최대 거리 내의 식당만 추가
            if (distance <= maxDistance) {
                Map<String, Object> restaurantWithDistance = new HashMap<>(restaurant);
                restaurantWithDistance.put("distance", distance);
                restaurantsWithDistance.add(restaurantWithDistance);
            }
        }

        // 거리순으로 정렬
        Collections.sort(restaurantsWithDistance,
                Comparator.comparingDouble(r -> (double) r.get("distance")));

        return restaurantsWithDistance;
    }

    /**
     * 두 좌표 간의 거리를 계산하는 메서드입니다.
     *
     * @param lat1 위도 1
     * @param lon1 경도 1
     * @param lat2 위도 2
     * @param lon2 경도 2
     * @return 두 지점 간의 거리 (단위: km)
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (단위: km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // 거리 (단위: km)
    }

}
