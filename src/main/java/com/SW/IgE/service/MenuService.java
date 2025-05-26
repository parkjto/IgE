package com.SW.IgE.service;

import com.SW.IgE.entity.Menu;
import com.SW.IgE.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public List<Menu> getMenusByCategory(String category) {
        return menuRepository.findByCategory(category);
    }

    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
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

        logger.info("Fetched random menu: {}", randomMenu.getName());
        return randomMenu;
    }

    public Menu getRandomMenuByCategory(String category) {
        List<String> categories;
        
        switch (category) {
            case "한식":
                categories = Arrays.asList("한식");
                break;
            case "양식":
                categories = Arrays.asList("양식");
                break;
            case "치킨":
                categories = Arrays.asList("치킨");
                break;
            case "피자":
                categories = Arrays.asList("피자");
                break;
            case "샐러드":
                categories = Arrays.asList("샐러드");
                break;
            default:
                return getRandomMenu(); // 전체 카테고리에서 랜덤 선택
        }
        
        return menuRepository.findRandomMenuByCategories(categories);
    }

}
