package com.shoppingmall.constant;

import java.util.Arrays;

public enum SocialType {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao"),
    TWITTER("twitter"),
    ;

    private final String name;

    SocialType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SocialType getSocialType(String registrationId) {
        return Arrays.stream(SocialType.values())
                .filter(socialType -> socialType.getName().equalsIgnoreCase(registrationId))
                .findFirst()
                .orElse(null);
    }
}
