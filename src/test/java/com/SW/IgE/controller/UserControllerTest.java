package com.SW.IgE.controller;

import com.SW.IgE.DTO.UserUpdateDTO;
import com.SW.IgE.entity.User;
import com.SW.IgE.service.UserDetailsServiceImpl;
import com.SW.IgE.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController 테스트")
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1)
                .useremail("test@example.com")
                .password("encodedPassword")
                .name("테스트 사용자")
                .age(25)
                .user_ige(Arrays.asList("계란", "우유"))
                .role(User.Role.USER)
                .build();
    }

    @Test
    @DisplayName("회원가입 - 정상적인 경우")
    void join_WhenValid_ReturnsOk() {
        // given
        User newUser = User.builder()
                .useremail("newuser@example.com")
                .password("password123")
                .name("새 사용자")
                .age(30)
                .build();

        when(userService.existsByUseremail(anyString())).thenReturn(false);
        doNothing().when(userService).joinUser(any(User.class));

        // when
        ResponseEntity<String> response = userController.join(newUser);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("완료"));
        verify(userService, times(1)).existsByUseremail(newUser.getUseremail());
        verify(userService, times(1)).joinUser(any(User.class));
    }

    @Test
    @DisplayName("회원가입 - 이미 존재하는 이메일인 경우")
    void join_WhenEmailExists_ReturnsConflict() {
        // given
        User newUser = User.builder()
                .useremail("existing@example.com")
                .password("password123")
                .name("새 사용자")
                .age(30)
                .build();

        when(userService.existsByUseremail(anyString())).thenReturn(true);

        // when
        ResponseEntity<String> response = userController.join(newUser);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().contains("이미 존재하는 이메일"));
        verify(userService, times(1)).existsByUseremail(newUser.getUseremail());
        verify(userService, never()).joinUser(any(User.class));
    }

    @Test
    @DisplayName("로그인 - 정상적인 경우")
    void login_WhenValid_ReturnsUserInfo() {
        // given
        Map<String, String> loginRequest = Map.of(
                "useremail", "test@example.com",
                "password", "password123"
        );

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("test@example.com")
                .password("encodedPassword")
                .authorities("ROLE_USER")
                .build();

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userService.getUserInfo(anyString())).thenReturn(testUser);
        when(userService.getUserAllergies(anyInt())).thenReturn(Arrays.asList("계란", "우유"));

        // when
        ResponseEntity<Map<String, Object>> response = userController.login(loginRequest);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUser.getUseremail(), response.getBody().get("useremail"));
        verify(userDetailsService, times(1)).loadUserByUsername("test@example.com");
        verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
    }

    @Test
    @DisplayName("로그인 - 비밀번호가 일치하지 않는 경우")
    void login_WhenPasswordMismatch_ReturnsUnauthorized() {
        // given
        Map<String, String> loginRequest = Map.of(
                "useremail", "test@example.com",
                "password", "wrongPassword"
        );

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("test@example.com")
                .password("encodedPassword")
                .authorities("ROLE_USER")
                .build();

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // when
        ResponseEntity<Map<String, Object>> response = userController.login(loginRequest);

        // then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("message"));
        verify(passwordEncoder, times(1)).matches("wrongPassword", "encodedPassword");
        verify(userService, never()).getUserInfo(anyString());
    }

    @Test
    @DisplayName("사용자 정보 업데이트 - 정상적인 경우")
    void updateUser_WhenValid_ReturnsUpdatedUser() {
        // given
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setId(1);
        updateDTO.setName("수정된 이름");
        updateDTO.setAge(30);
        updateDTO.setAllergies(Arrays.asList("콩"));

        User updatedUser = User.builder()
                .id(1)
                .useremail("test@example.com")
                .name("수정된 이름")
                .age(30)
                .user_ige(Arrays.asList("콩"))
                .role(User.Role.USER)
                .build();

        when(userService.updateUser(any(UserUpdateDTO.class))).thenReturn(updatedUser);
        when(userService.getUserAllergies(anyInt())).thenReturn(Arrays.asList("콩"));

        // when
        ResponseEntity<?> response = userController.updateUser(updateDTO, null);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).updateUser(updateDTO);
    }

    @Test
    @DisplayName("사용자 정보 업데이트 - ID가 없는 경우")
    void updateUser_WhenIdIsNull_ReturnsBadRequest() {
        // given
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setId(null);
        updateDTO.setName("수정된 이름");

        // when
        ResponseEntity<?> response = userController.updateUser(updateDTO, null);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("ID는 필수"));
        verify(userService, never()).updateUser(any(UserUpdateDTO.class));
    }
}

