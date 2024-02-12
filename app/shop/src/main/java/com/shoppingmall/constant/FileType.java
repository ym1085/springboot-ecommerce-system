package com.shoppingmall.constant;

public enum FileType {
    POSTS("posts"),
    PRODUCTS("products"),
    SHOP("shop"),
    ;

    private final String fileTypeName;

    FileType(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }
}
