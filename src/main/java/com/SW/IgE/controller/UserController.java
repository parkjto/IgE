package com.SW.IgE.controller;

import com.SW.IgE.DTO.UserUpdateDTO;
import com.SW.IgE.entity.User;
import com.SW.IgE.repository.UserRepository;
import com.SW.IgE.service.UserDetailsServiceImpl;
import com.SW.IgE.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class UserController {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody User user) {
        if (userService.existsByUseremail(user.getUseremail())) {
            logger.info("이미 존재하는 이메일입니다: {}", user.getUseremail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
        }
        userService.joinUser(user);
        logger.info("회원가입 성공: {}", user.getUseremail());
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String useremail = loginRequest.get("useremail");
        String password = loginRequest.get("password");

        if (useremail == null || useremail.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "이메일과 비밀번호는 필수 입력입니다."));
        }

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(useremail);
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                User user = userService.getUserInfo(useremail);
                List<String> allergies = userService.getUserAllergies(user.getId());

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", user.getId());
                userInfo.put("useremail", userDetails.getUsername());
                userInfo.put("role", userDetails.getAuthorities().toString());
                userInfo.put("allergies", allergies);
                userInfo.put("name", user.getName());
                userInfo.put("age", user.getAge());

                logger.info("로그인 성공: {}", useremail);
                return ResponseEntity.ok(userInfo);
            } else {
                logger.warn("로그인 실패 - 잘못된 비밀번호: {}", useremail);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "이메일 또는 비밀번호가 잘못되었습니다."));
            }
        } catch (UsernameNotFoundException e) {
            logger.warn("로그인 실패 - 이메일 찾을 수 없음: {}", useremail);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "이메일 또는 비밀번호가 잘못되었습니다."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        logger.info("로그아웃 완료");
        return ResponseEntity.ok("로그아웃이 완료되었습니다.");
    }

    @GetMapping("/userInfo")
    public ResponseEntity<Map<String, Object>> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (currentUserEmail == null || currentUserEmail.isEmpty()) {
            logger.warn("사용자 인증 실패 - 사용자 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "인증된 사용자가 아닙니다."));
        }

        User user = userService.getUserInfo(currentUserEmail);
        if (user == null) {
            logger.warn("사용자 정보 조회 실패 - 사용자 없음: {}", currentUserEmail);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "사용자를 찾을 수 없습니다."));
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("name", user.getName());
        userInfo.put("useremail", user.getUseremail());
        userInfo.put("age", user.getAge());
        userInfo.put("role", user.getRole().toString());
        userInfo.put("user_ige", user.getUser_ige());

        logger.info("사용자 정보 조회 성공: {}", currentUserEmail);
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO.getId() == null) {
            return ResponseEntity.badRequest().body("사용자 ID는 필수입니다.");
        }

        try {
            // 비밀번호가 변경된 경우
            if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
                // 비밀번호 강도 체크 로직 추가 가능 (예: 길이, 특수문자 포함 등)
                if (userUpdateDTO.getPassword().length() < 2) {
                    return ResponseEntity.badRequest().body("비밀번호는 최소 2자 이상이어야 합니다.");
                }
            }

            User updatedUser = userService.updateUser(userUpdateDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            logger.error("사용자 정보 업데이트 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업데이트 실패: " + e.getMessage());
        }
    }


    @GetMapping("/admin")
    public ResponseEntity<String> getAdminPage() {
        return ResponseEntity.ok("어드민 페이지 접근 성공");
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserInfo(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
