package com.SW.IgE.service;

import com.SW.IgE.entity.Menu;
import com.SW.IgE.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Menu getRandomMenu() {
        // minId와 maxId를 먼저 조회
        Integer minId = menuRepository.findMinId();
        Integer maxId = menuRepository.findMaxId();

        if (minId == null || maxId == null) {
            // 데이터베이스에 메뉴가 없을 경우 예외를 던지도록 처리
            logger.error("No menu data found in the database.");
            throw new IllegalStateException("메뉴가 존재하지 않습니다.");
        }

        // minId와 maxId 사이에서 랜덤 ID 생성 (Math.random()을 사용)
        int randomId = (int) (Math.random() * (maxId - minId + 1)) + minId;

        // 생성된 랜덤 ID로 메뉴 조회
        Menu randomMenu = menuRepository.findMenuById(randomId);

        if (randomMenu == null) {
            logger.error("Random menu not found with id {}, returning error.", randomId);
            throw new IllegalStateException("랜덤 메뉴를 찾을 수 없습니다.");
        }

        logger.info("Fetched random menu: {}", randomMenu.getMenu_name());
        return randomMenu;
    }

    public Menu getRandomMenuByCategory(String category) {
        List<String> foodTypes;
        
        switch (category) {
            case "한식":
                foodTypes = Arrays.asList("밑반찬", "밥/죽/떡", "메인반찬", "찌개", "국/탕", "김치/젓갈/장류");
                break;
            case "양식":
                foodTypes = Arrays.asList("양식", "스프");
                break;
            case "간식":
                foodTypes = Arrays.asList("디저트", "빵", "과자");
                break;
            case "음료":
                foodTypes = Arrays.asList("차/음료/술");
                break;
            case "기타":
                foodTypes = Arrays.asList("양념/소스/잼", "퓨전");
                break;
            default:
                return getRandomMenu(); // 전체 카테고리에서 랜덤 선택
        }
        
        return menuRepository.findRandomMenuByFoodTypes(foodTypes);
    }

}
