package com.shoppingmall.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

// https://engineerinsight.tistory.com/79
// https://sudong.tistory.com/72
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${os.window.upload-path}")
    private String uploadPathByWindow;

    @Value("${os.mac.upload-path}")
    private String uploadPathByMac;

    @Value("${os.linux.upload-path}")
    private String uploadPathByLinux;

    private static final String[] templateFolders = {"classpath:/templates/", "classpath:/static/", "classpath:/images/"};
    private static final List<String> imageFolders = Arrays.asList("shop", "board");

    // JAR 배포 후 해당 경로 접근이 안되기에 -> /** 요청 -> 아래 경로로 URL 매핑을 해준다
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //WebMvcConfigurer.super.addResourceHandlers(registry);
        // 정적 static template 경로 매핑
        registry.addResourceHandler("/**")
                .addResourceLocations(templateFolders);

        // 외부 파일 경로 URL 매핑
        // ---> [개발] localhost:8080/static/img/shop/**
        // ---> [운영] domain.co.kr/static/img/shop/**
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            addResourceLocationsForWindow(registry);
        } else if (os.contains("mac")) {
            addResourceLocationsForMac(registry);
        } else if (os.contains("linux")) {
            addResourceLocationsForLinux(registry);
        }
    }

    private void addResourceLocationsForWindow(ResourceHandlerRegistry registry) {
        for (String imageFolder : imageFolders) {
            registry.addResourceHandler("/static/img/" + imageFolder + "/**")
                    .addResourceLocations("file:///" + uploadPathByWindow + imageFolder + "/");
        }
    }

    private void addResourceLocationsForMac(ResourceHandlerRegistry registry) {
        for (String imageFolder : imageFolders) {
            registry.addResourceHandler("/static/img/" + imageFolder + "/**")
                    .addResourceLocations("file:///" + uploadPathByMac + imageFolder + "/");
        }
        log.debug("registry = {}", registry);
    }

    private void addResourceLocationsForLinux(ResourceHandlerRegistry registry) {
        for (String imageFolder : imageFolders) {
            registry.addResourceHandler("/static/img/" + imageFolder + "/**")
                    .addResourceLocations("file:///" + uploadPathByLinux + imageFolder + "/");
        }
    }
}