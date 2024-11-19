package com.SW.IgE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {  // 클래스 이름을 변경했습니다.

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
