package com.shoppingmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 서버가 응답을 할 때 json을 JS에서 처리할 수 있게 할지 설정. 즉, axios, fetch 요청에 대한 응답을 받을 수 있게 할것인지
        config.addAllowedOrigin("*"); // 모든 ip의 응답 허용
        config.addAllowedHeader("*"); // 모든 HTTP HEADER 응답 허용
        config.addAllowedMethod("*"); // 모든 HTTP METHOD 요청 허용
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
