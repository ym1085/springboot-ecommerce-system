package com.multi.posts.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommonUtils {

    public static boolean isNumericPattern(Long id) {
        return String.valueOf(id).matches("^[0-9]+$");
    }
}
