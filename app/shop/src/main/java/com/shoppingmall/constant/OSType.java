package com.shoppingmall.constant;

public enum OSType {
    WINDOW("win"),
    MAC("mac"),
    LINUX("linux"),
    ;

    private final String osName;

    OSType(String osName) {
        this.osName = osName;
    }

    public String getOsName() {
        return osName;
    }
}
