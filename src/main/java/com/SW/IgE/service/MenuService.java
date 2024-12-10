package com.SW.IgE.service;

import com.SW.IgE.entity.Menu;
import com.SW.IgE.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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


}
