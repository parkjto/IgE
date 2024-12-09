package com.SW.IgE.controller;

import com.SW.IgE.service.NaverSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/search")
public class MServerController {

    private static final Logger LOGGER = Logger.getLogger(MServerController.class.getName());

    private final NaverSearchService naverSearchService;

    public MServerController(NaverSearchService naverSearchService) {
        this.naverSearchService = naverSearchService;
    }

    /**
     * 네이버 API로 장소 검색을 처리하는 엔드포인트.
     *
     * @param query 검색할 장소 이름
     * @return 검색된 장소 목록
     */
    @GetMapping("/naver")
    public ResponseEntity<?> searchRestaurants(@RequestParam String query) {
        LOGGER.log(Level.INFO, "searchRestaurants called. Query: {0}", query);

        List<Map<String, String>> restaurants = naverSearchService.searchRestaurants(query);

        if (restaurants.isEmpty()) {
            LOGGER.log(Level.WARNING, "No restaurants found for query: {0}", query);
            return ResponseEntity.noContent().build(); // 결과가 없으면 204 No Content 반환
        } else {
            LOGGER.log(Level.INFO, "Number of restaurants found: {0}", restaurants.size());
            return ResponseEntity.ok(restaurants); // 검색된 식당 목록 반환
        }
    }

    /**
     * 거리 계산을 포함하여 네이버 API로 장소 검색을 처리하는 엔드포인트.
     *
     * @param query 검색할 장소 이름
     * @param userX 사용자 위치의 경도 (X 좌표)
     * @param userY 사용자 위치의 위도 (Y 좌표)
     * @return 검색된 장소 목록과 거리
     */
    @GetMapping("/naver/distance")
    public List<Map<String, Object>> searchWithDistance(
            @RequestParam("query") String query,
            @RequestParam("userX") double userX,
            @RequestParam("userY") double userY,
            @RequestParam(value = "maxDistance", defaultValue = "3") double maxDistance) {

        LOGGER.log(Level.INFO, "searchWithDistance called. Query: {0}, User position: ({1}, {2})", new Object[]{query, userX, userY});

        List<Map<String, String>> restaurants = naverSearchService.searchRestaurants(query);

        if (restaurants.isEmpty()) {
            LOGGER.log(Level.WARNING, "No restaurants found for query: {0}", query);
        } else {
            LOGGER.log(Level.INFO, "Number of restaurants found: {0}", restaurants.size());
        }

        List<Map<String, String>> result = naverSearchService.searchRestaurants(query);

        LOGGER.log(Level.INFO, "Distance calculation completed. Number of results (with distance): {0}", result.size());

        return naverSearchService.searchRestaurantsWithDistance(result, userX, userY, maxDistance);
    }

}
