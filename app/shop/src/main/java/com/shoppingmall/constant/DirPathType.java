package com.shoppingmall.constant;

public enum DirPathType {
    posts("posts"),
    products("products"),
    ;

    private final String dirPathTypeName;

    DirPathType(String fileTypeName) {
        this.dirPathTypeName = fileTypeName;
    }

    public String getDirPathTypeName() {
        return dirPathTypeName;
    }
}
