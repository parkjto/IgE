package com.SW.IgE;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 요청에 대해 CORS 허용
                .allowedOrigins("http://localhost:3000") // React 애플리케이션의 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
