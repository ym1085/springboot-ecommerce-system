package com.shoppingmall.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// https://engineerinsight.tistory.com/79
// https://sudong.tistory.com/72
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String[] classPathTemplatePath = {
            "classpath:/templates/",
            "classpath:/static/",
            "classpath:/images/"
    };

    // JAR 배포 후 해당 경로 접근이 안되기에 -> /** 요청 -> 아래 경로로 URL 매핑을 해준다
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(classPathTemplatePath);

        // /upload/** 로 시작하는 모든 요청들에 대하여 외부 경로로 파일 경로 매핑
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///"); // 실제 파일 저장 경로
    }
}