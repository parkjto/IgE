package com.SW.IgE.controller;

import com.SW.IgE.entity.Menu;
import com.SW.IgE.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin(origins = "http://localhost:3000") // React 앱이 실행되는 포트를 지정합니다.
public class MenuController {

    private final MenuService menuService;
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<Menu> getAllMenus() {
        logger.info("Fetching all menus");
        return menuService.getAllMenus(); // 전체 메뉴 정보를 반환
    }
}
