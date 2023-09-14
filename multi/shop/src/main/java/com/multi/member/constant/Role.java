package com.multi.member.constant;

public enum Role {
    ROLE_USER("ROLE_USER", "일반 사용자"),
    ROLE_GUEST("ROLE_GUEST", "손님"),
    ROLE_ADMIN("ROLE_ADMIN", "관리자"),
    ;

    private final String key;
    private final String title;

    Role(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }
}
