package com.SW.IgE.service;

import com.SW.IgE.constants.AllergyConstants;
import com.SW.IgE.entity.User;
import com.SW.IgE.exception.UserNotFoundException;
import com.SW.IgE.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

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
    @DisplayName("이메일 존재 여부 확인 - 존재하는 경우")
    void existsByUseremail_WhenExists_ReturnsTrue() {
        // given
        String email = "test@example.com";
        when(userRepository.existsByUseremail(email)).thenReturn(true);

        // when
        boolean result = userService.existsByUseremail(email);

        // then
        assertTrue(result);
        verify(userRepository, times(1)).existsByUseremail(email);
    }

    @Test
    @DisplayName("이메일 존재 여부 확인 - 존재하지 않는 경우")
    void existsByUseremail_WhenNotExists_ReturnsFalse() {
        // given
        String email = "nonexistent@example.com";
        when(userRepository.existsByUseremail(email)).thenReturn(false);

        // when
        boolean result = userService.existsByUseremail(email);

        // then
        assertFalse(result);
        verify(userRepository, times(1)).existsByUseremail(email);
    }

    @Test
    @DisplayName("회원가입 - 정상적인 경우")
    void joinUser_WhenValid_ShouldSaveUser() {
        // given
        User newUser = User.builder()
                .useremail("newuser@example.com")
                .password("password123")
                .name("새 사용자")
                .age(30)
                .user_ige(Arrays.asList("콩"))
                .build();

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // when
        userService.joinUser(newUser);

        // then
        verify(bCryptPasswordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(User.Role.USER, newUser.getRole());
    }

    @Test
    @DisplayName("회원가입 - 비밀번호가 너무 짧은 경우 예외 발생")
    void joinUser_WhenPasswordTooShort_ThrowsException() {
        // given
        User newUser = User.builder()
                .useremail("newuser@example.com")
                .password("123") // 최소 길이 미만
                .name("새 사용자")
                .age(30)
                .build();

        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.joinUser(newUser)
        );

        assertTrue(exception.getMessage().contains("비밀번호는 최소"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 - 비밀번호가 null인 경우 예외 발생")
    void joinUser_WhenPasswordIsNull_ThrowsException() {
        // given
        User newUser = User.builder()
                .useremail("newuser@example.com")
                .password(null)
                .name("새 사용자")
                .age(30)
                .build();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> userService.joinUser(newUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 - 알레르기 정보가 null인 경우 빈 리스트로 초기화")
    void joinUser_WhenAllergiesIsNull_ShouldInitializeEmptyList() {
        // given
        User newUser = User.builder()
                .useremail("newuser@example.com")
                .password("password123")
                .name("새 사용자")
                .age(30)
                .user_ige(null)
                .build();

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertNotNull(savedUser.getUser_ige());
            assertTrue(savedUser.getUser_ige().isEmpty());
            return savedUser;
        });

        // when
        userService.joinUser(newUser);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 정보 조회 - 정상적인 경우")
    void getUserInfo_WhenExists_ReturnsUser() {
        // given
        String email = "test@example.com";
        when(userRepository.findByUseremail(email)).thenReturn(Optional.of(testUser));

        // when
        User result = userService.getUserInfo(email);

        // then
        assertNotNull(result);
        assertEquals(testUser.getUseremail(), result.getUseremail());
        assertEquals(testUser.getName(), result.getName());
        verify(userRepository, times(1)).findByUseremail(email);
    }

    @Test
    @DisplayName("사용자 정보 조회 - 존재하지 않는 경우 예외 발생")
    void getUserInfo_WhenNotExists_ThrowsException() {
        // given
        String email = "nonexistent@example.com";
        when(userRepository.findByUseremail(email)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.getUserInfo(email));
        verify(userRepository, times(1)).findByUseremail(email);
    }

    @Test
    @DisplayName("사용자 알레르기 정보 조회 - 정상적인 경우")
    void getUserAllergies_WhenExists_ReturnsAllergies() {
        // given
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // when
        List<String> result = userService.getUserAllergies(userId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("계란"));
        assertTrue(result.contains("우유"));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("사용자 알레르기 정보 조회 - 사용자가 존재하지 않는 경우 빈 리스트 반환")
    void getUserAllergies_WhenNotExists_ReturnsEmptyList() {
        // given
        Integer userId = 999;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        List<String> result = userService.getUserAllergies(userId);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findById(userId);
    }
}





























