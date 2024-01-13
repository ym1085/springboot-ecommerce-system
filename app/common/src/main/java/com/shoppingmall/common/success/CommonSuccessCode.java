package com.shoppingmall.common.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonSuccessCode implements SuccessCode {
    SUCCESS_CODE(HttpStatus.OK, "200 OK"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
