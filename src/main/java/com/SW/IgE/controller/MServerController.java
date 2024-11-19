package com.SW.IgE.controller;

import com.SW.IgE.service.NaverSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class MServerController {

    private final NaverSearchService naverSearchService;

    public MServerController(NaverSearchService naverSearchService) {
        this.naverSearchService = naverSearchService;
    }

    // 이름 기반 검색
    @GetMapping("/naver/{name}")
    public List<Map<String, String>> searchByName(@PathVariable String name) {
        return naverSearchService.searchRestaurants(name);
    }

    // 쿼리 파라미터 기반 검색
    @GetMapping("/naver")
    public List<Map<String, String>> searchByQuery(@RequestParam("query") String query) {
        return naverSearchService.searchRestaurants(query);
    }
}
