package com.SW.IgE.service;

import com.SW.IgE.entity.Menu;
import com.SW.IgE.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        logger.info("Fetched {} menus", menus.size());
        return menus;
    }
}
