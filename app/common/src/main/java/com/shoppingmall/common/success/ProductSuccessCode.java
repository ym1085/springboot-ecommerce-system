package com.shoppingmall.common.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductSuccessCode implements SuccessCode {

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
