package com.SW.IgE.controller;

import com.SW.IgE.DTO.UserUpdateDTO;
import com.SW.IgE.entity.User;
import com.SW.IgE.exception.UserNotFoundException;
import com.SW.IgE.repository.UserRepository;
import com.SW.IgE.service.UserDetailsServiceImpl;
import com.SW.IgE.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class UserController {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 처리
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

    // 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String useremail = loginRequest.get("useremail");
        String password = loginRequest.get("password");

        logger.info("Login attempt for email: {}", useremail);

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(useremail);
            String storedPassword = userDetails.getPassword();
            
            logger.info("Attempting password match for user: {}", useremail);
            boolean matches = passwordEncoder.matches(password, storedPassword);
            logger.info("Password match result: {}", matches);

            if (matches) {
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
                logger.warn("Password match failed for user: {}", useremail);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "이메일 또는 비밀번호가 잘못되었습니다."));
            }
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "로그인 실패"));
        }
    }

    // 로그아웃 처리
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        logger.info("로그아웃 완료");
        return ResponseEntity.ok("로그아웃이 완료되었습니다.");
    }

    // 사용자 정보 조회
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

        List<String> allergies = userService.getUserAllergies(user.getId());
        logger.info("Retrieved allergies for user {}: {}", user.getId(), allergies);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("name", user.getName());
        userInfo.put("useremail", user.getUseremail());
        userInfo.put("age", user.getAge());
        userInfo.put("role", user.getRole().toString());
        userInfo.put("allergies", allergies);
        userInfo.put("user_ige", allergies);

        logger.info("사용자 정보 조회 성공. 전체 정보: {}", userInfo);
        return ResponseEntity.ok(userInfo);
    }

    // 사용자 정보 업데이트
    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDTO userUpdateDTO, HttpServletRequest request) {
        logger.info("[Update] HTTP Method: {}", request.getMethod());
        logger.info("[Update] Content-Type: {}", request.getContentType());
        logger.info("[Update] 요청 데이터 전체 내용: {}", userUpdateDTO);
        logger.info("[Update] 요청 헤더: {}", Collections.list(request.getHeaderNames())
            .stream()
            .collect(Collectors.toMap(h -> h, request::getHeader)));

        if (userUpdateDTO.getId() == null) {
            logger.warn("[Update] 사용자 ID가 누락됨");
            return ResponseEntity.badRequest().body("사용자 ID는 필수입니다.");
        }

        try {
            User updatedUser = userService.updateUser(userUpdateDTO);
            List<String> allergies = userService.getUserAllergies(updatedUser.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("id", updatedUser.getId());
            response.put("name", updatedUser.getName());
            response.put("age", updatedUser.getAge());
            response.put("useremail", updatedUser.getUseremail());
            response.put("role", updatedUser.getRole().toString());
            response.put("user_ige", allergies);
            response.put("allergies", allergies);

            logger.info("[Update] 사용자 정보 업데이트 성공: {}", updatedUser);
            logger.info("[Update] 응답 데이터: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("[Update] 업데이트 중 오류 발생", e);
            logger.error("[Update] 상세 에러 메시지: {}", e.getMessage());
            logger.error("[Update] 에러 스택트레이스: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("업데이트 실패: " + e.getMessage());
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

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, Object> request) {
        logger.info("[ResetPassword] 비밀번호 재설정 요청 시작");
        
        try {
            String useremail = (String) request.get("useremail");
            String name = (String) request.get("name");
            String newPassword = (String) request.get("newPassword");
            Integer age = Integer.parseInt(request.get("age").toString());
            @SuppressWarnings("unchecked")
            List<String> allergies = (List<String>) request.get("allergies");
            
            if (useremail == null || name == null || newPassword == null || allergies == null) {
                return ResponseEntity.badRequest().body("모든 필드를 입력해주세요.");
            }
            
            String result = userService.resetPassword(useremail, name, age, allergies, newPassword);
            return ResponseEntity.ok(result);
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("올바른 나이를 입력해주세요.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("[ResetPassword] 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("비밀번호 재설정 중 오류가 발생했습니다.");
        }
    }
}
