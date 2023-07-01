package com.multi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// https://engineerinsight.tistory.com/79
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // Todo: images 경로 외부 경로로 수정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
