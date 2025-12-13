package com.SW.IgE.service;

import com.SW.IgE.entity.Menu;
import com.SW.IgE.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuService 테스트")
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    private Menu testMenu;

    @BeforeEach
    void setUp() {
        testMenu = Menu.builder()
                .id(1)
                .name("불고기")
                .description("한국의 전통적인 구이 요리")
                .price(15000.0)
                .category("한식")
                .imageUrl("/images/bulgogi.jpg")
                .allergens(Arrays.asList("대두", "밀"))
                .build();
    }

    @Test
    @DisplayName("전체 메뉴 조회 - 정상적인 경우")
    void getAllMenus_WhenExists_ReturnsAllMenus() {
        // given
        List<Menu> menus = Arrays.asList(testMenu);
        when(menuRepository.findAll()).thenReturn(menus);

        // when
        List<Menu> result = menuService.getAllMenus();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMenu.getName(), result.get(0).getName());
        verify(menuRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("카테고리별 메뉴 조회 - 정상적인 경우")
    void getMenusByCategory_WhenExists_ReturnsMenus() {
        // given
        String category = "한식";
        List<Menu> menus = Arrays.asList(testMenu);
        when(menuRepository.findByCategory(category)).thenReturn(menus);

        // when
        List<Menu> result = menuService.getMenusByCategory(category);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(category, result.get(0).getCategory());
        verify(menuRepository, times(1)).findByCategory(category);
    }

    @Test
    @DisplayName("메뉴 저장 - 정상적인 경우")
    void saveMenu_WhenValid_ReturnsSavedMenu() {
        // given
        Menu newMenu = Menu.builder()
                .name("새 메뉴")
                .description("설명")
                .price(10000.0)
                .category("양식")
                .imageUrl("/images/new.jpg")
                .allergens(Arrays.asList("우유"))
                .build();

        when(menuRepository.save(any(Menu.class))).thenReturn(newMenu);

        // when
        Menu result = menuService.saveMenu(newMenu);

        // then
        assertNotNull(result);
        assertEquals(newMenu.getName(), result.getName());
        verify(menuRepository, times(1)).save(newMenu);
    }

    @Test
    @DisplayName("랜덤 메뉴 조회 - 정상적인 경우")
    void getRandomMenu_WhenExists_ReturnsRandomMenu() {
        // given
        Integer minId = 1;
        Integer maxId = 10;
        int randomId = 5;

        when(menuRepository.findMinId()).thenReturn(minId);
        when(menuRepository.findMaxId()).thenReturn(maxId);
        when(menuRepository.findMenuById(anyInt())).thenReturn(testMenu);

        // when
        Menu result = menuService.getRandomMenu();

        // then
        assertNotNull(result);
        verify(menuRepository, times(1)).findMinId();
        verify(menuRepository, times(1)).findMaxId();
        verify(menuRepository, atLeastOnce()).findMenuById(anyInt());
    }

    @Test
    @DisplayName("랜덤 메뉴 조회 - 메뉴가 없는 경우 예외 발생")
    void getRandomMenu_WhenNoMenus_ThrowsException() {
        // given
        when(menuRepository.findMinId()).thenReturn(null);
        when(menuRepository.findMaxId()).thenReturn(null);

        // when & then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> menuService.getRandomMenu()
        );

        assertTrue(exception.getMessage().contains("메뉴가 존재하지 않습니다"));
        verify(menuRepository, never()).findMenuById(anyInt());
    }

    @Test
    @DisplayName("랜덤 메뉴 조회 - 랜덤 ID로 메뉴를 찾을 수 없는 경우 예외 발생")
    void getRandomMenu_WhenRandomIdNotFound_ThrowsException() {
        // given
        Integer minId = 1;
        Integer maxId = 10;

        when(menuRepository.findMinId()).thenReturn(minId);
        when(menuRepository.findMaxId()).thenReturn(maxId);
        when(menuRepository.findMenuById(anyInt())).thenReturn(null);

        // when & then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> menuService.getRandomMenu()
        );

        assertTrue(exception.getMessage().contains("랜덤 메뉴를 찾을 수 없습니다"));
    }

    @Test
    @DisplayName("카테고리별 랜덤 메뉴 조회 - 한식 카테고리")
    void getRandomMenuByCategory_WhenKoreanFood_ReturnsMenu() {
        // given
        String category = "한식";
        when(menuRepository.findRandomMenuByCategories(anyList())).thenReturn(testMenu);

        // when
        Menu result = menuService.getRandomMenuByCategory(category);

        // then
        assertNotNull(result);
        verify(menuRepository, times(1)).findRandomMenuByCategories(anyList());
    }

    @Test
    @DisplayName("카테고리별 랜덤 메뉴 조회 - 알 수 없는 카테고리인 경우 전체에서 랜덤 선택")
    void getRandomMenuByCategory_WhenUnknownCategory_ReturnsRandomFromAll() {
        // given
        String category = "알수없음";
        Integer minId = 1;
        Integer maxId = 10;

        when(menuRepository.findMinId()).thenReturn(minId);
        when(menuRepository.findMaxId()).thenReturn(maxId);
        when(menuRepository.findMenuById(anyInt())).thenReturn(testMenu);

        // when
        Menu result = menuService.getRandomMenuByCategory(category);

        // then
        assertNotNull(result);
        verify(menuRepository, times(1)).findMinId();
        verify(menuRepository, times(1)).findMaxId();
    }
}





























