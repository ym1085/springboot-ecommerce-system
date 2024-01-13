package com.shoppingmall.common.success;

import org.springframework.http.HttpStatus;

public interface SuccessCode {
    String name();

    HttpStatus getHttpStatus();

    String getMessage();
}
