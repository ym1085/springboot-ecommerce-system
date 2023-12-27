package com.shoppingmall.constant;

public enum RedisKey {
    REGISTER("Register_");

    private String key;

    RedisKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
