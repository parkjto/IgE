package com.SW.IgE.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS 설정을 위한 필터
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // Spring Security 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        configureCsrf(http);   // CSRF 설정
        configureAuthorization(http);  // 권한 설정
        configureFormLogin(http);  // 로그인 설정
        configureLogout(http);  // 로그아웃 설정
        configureSessionManagement(http);  // 세션 관리 설정

        // CORS 필터를 인증 필터 앞에 추가
        http.addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CSRF 설정
    private void configureCsrf(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());  // CSRF 보호를 완전히 비활성화
    }

    // 권한 설정
    private void configureAuthorization(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/login", "/loginProc", "/join", "/error", "/update").permitAll()
                .requestMatchers("/userInfo", "/Mypage").authenticated()
                .requestMatchers("/user/**").authenticated()
                .anyRequest().permitAll());
    }

    // 폼 로그인 설정
    private void configureFormLogin(HttpSecurity http) throws Exception {
        http.formLogin(formLogin -> formLogin
                .loginPage("/login")  // 로그인 페이지 URL
                .loginProcessingUrl("/loginProc")  // 로그인 처리 URL
                .usernameParameter("useremail")  // 로그인 폼의 사용자 이름 파라미터명 설정
                .successHandler((request, response, authentication) -> {
                    if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                        response.sendRedirect("/admin/dashboard");  // 관리자 페이지로 리다이렉트
                    } else {
                        response.sendRedirect("/user/home");  // 사용자 홈 페이지로 리다이렉트
                    }
                })
                .defaultSuccessUrl("/loginOk", true));  // 로그인 성공 시 기본 리다이렉트 경로
    }

    // 로그아웃 설정
    private void configureLogout(HttpSecurity http) throws Exception {
        http.logout(logout -> logout
                .logoutUrl("/logout")  // 로그아웃 처리 URL
                .logoutSuccessUrl("/logoutOk")  // 로그아웃 성공 시 리다이렉트 URL
                .deleteCookies("JSESSIONID"));  // 로그아웃 후 세션 쿠키 삭제
    }

    // 세션 관리 설정
    private void configureSessionManagement(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
    }
}
