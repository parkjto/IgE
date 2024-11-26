package com.SW.IgE.controller;

import com.SW.IgE.entity.User;
import com.SW.IgE.service.UserDetailsServiceImpl;
import com.SW.IgE.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        // 현재 인증된 사용자의 이메일 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // 사용자 정보 가져오기
        User user = userService.getUserInfo(currentUserEmail);
        if (user == null) {
            logger.warn("사용자 정보 조회 실패 - 사용자 없음: {}", currentUserEmail);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "사용자를 찾을 수 없습니다."));
        }

        // JSON 응답 데이터 생성
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", user.getName());
        userInfo.put("useremail", user.getUseremail());
        userInfo.put("age", user.getAge());
        userInfo.put("role", user.getRole().toString()); // 문자열로 변환하여 role 반환
        userInfo.put("user_ige", user.getUser_ige()); // 알레르기 목록

        logger.info("사용자 정보 조회 성공: {}", currentUserEmail);
        return ResponseEntity.ok(userInfo);
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

    @PostMapping("/updateUser")
    public ResponseEntity<Map<String, Object>> updateUser(@Valid @RequestBody Map<String, Object> updatedData) {
        // 현재 인증된 사용자의 이메일 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // 현재 사용자의 정보를 가져오기
        User currentUser = userService.getUserInfo(currentUserEmail);
        if (currentUser == null) {
            logger.warn("사용자 정보 수정 실패 - 사용자 없음: {}", currentUserEmail);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "사용자를 찾을 수 없습니다."));
        }

        // 수정할 데이터 반영
        if (updatedData.containsKey("name")) {
            currentUser.setName((String) updatedData.get("name"));
        }
        if (updatedData.containsKey("age")) {
            Integer age = (Integer) updatedData.get("age");
            if (age != null && age > 0) {
                currentUser.setAge(age);
            }
        }
        if (updatedData.containsKey("user_ige")) {
            List<String> allergies = (List<String>) updatedData.get("user_ige");
            if (allergies != null) {
                currentUser.setUser_ige(allergies);
            }
        }

        // 사용자 정보 업데이트
        userService.updateUser(currentUser);

        logger.info("사용자 정보 수정 성공: {}", currentUserEmail);

        // JSON 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("message", "사용자 정보가 성공적으로 수정되었습니다.");
        response.put("user", currentUser);

        return ResponseEntity.ok(response);
    }



    @GetMapping("/user/details")
    public ResponseEntity<User> getUserDetails(@RequestParam String useremail) {
        User user = userService.getUserWithAllDetails(useremail);
        if (user != null) {
            logger.info("사용자 상세 정보 조회 성공: {}", useremail);
            return ResponseEntity.ok(user);
        } else {
            logger.warn("사용자 상세 정보 조회 실패 - 사용자 없음: {}", useremail);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
