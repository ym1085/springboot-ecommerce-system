package com.shoppingmall.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
    posts("posts"),
    products("products"),
    ;

    private final String fileType;
}