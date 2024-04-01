package com.shoppingmall.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class CommonUtils {

    public static String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }
}