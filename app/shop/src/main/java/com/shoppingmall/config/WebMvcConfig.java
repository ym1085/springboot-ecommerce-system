package com.shoppingmall.config;

import com.shoppingmall.constant.OSType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    private static final String resourcePath = "/upload/"; // view에서 접근할 경로
    private static final String[] classPathTemplatePath = {"classpath:/templates/", "classpath:/static/", "classpath:/images/"};
    private static final List<String> resourceAddDirPath = Arrays.asList("shop", "posts");

    // JAR 배포 후 해당 경로 접근이 안되기에 -> /** 요청 -> 아래 경로로 URL 매핑을 해준다
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(classPathTemplatePath);

        String osPath = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains(OSType.WINDOW.getOsName())) {
            osPath = uploadPathByWindow;
        } else if (osName.contains(OSType.MAC.getOsName())) {
            osPath = uploadPathByMac;
        } else if (osName.contains(OSType.LINUX.getOsName())) {
            osPath = uploadPathByLinux;
        }

        // Todo: 다른 OS는 어떻게 구분 할지 고민 필요

        if (StringUtils.isNotBlank(osPath)) {
            addResourceLocations(registry, osPath);
        }
    }

    private void addResourceLocations(ResourceHandlerRegistry registry, String osPath) {
        for (String domain : resourceAddDirPath) {
            registry.addResourceHandler(resourcePath + domain + "/**")
                    .addResourceLocations("file:///" + osPath + domain + "/"); // 실제 파일 저장 경로
        }
    }
}