package com.SW.IgE.controller;

import com.SW.IgE.entity.Menu;
import com.SW.IgE.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin(origins = "http://localhost:3000") // React 앱이 실행되는 포트를 지정합니다.
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @GetMapping("/random")
    public Menu getRandomMenu() {
        logger.info("Fetching random menu");

        try {
            return menuService.getRandomMenu();
        } catch (IllegalStateException e) {
            logger.error("Error fetching random menu: {}", e.getMessage());
            return null;  // 예외가 발생한 경우 null을 반환하거나, 적절한 오류 메시지 반환
        }
    }

    @GetMapping("/random/{category}")
    public Menu getRandomMenuByCategory(@PathVariable String category) {
        logger.info("Fetching random menu for category: {}", category);
        try {
            return menuService.getRandomMenuByCategory(category);
        } catch (IllegalStateException e) {
            logger.error("Error fetching random menu for category {}: {}", category, e.getMessage());
            return null;
        }
    }

    @GetMapping
    public ResponseEntity<List<Menu>> getAllMenus() {
        logger.info("Fetching all menus");
        return ResponseEntity.ok(menuService.getAllMenus());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Menu>> getMenusByCategory(@PathVariable String category) {
        logger.info("Fetching menus for category: {}", category);
        return ResponseEntity.ok(menuService.getMenusByCategory(category));
    }

    @PostMapping
    public ResponseEntity<Menu> createMenu(@RequestBody Menu menu) {
        logger.info("Creating new menu");
        return ResponseEntity.ok(menuService.saveMenu(menu));
    }
}
