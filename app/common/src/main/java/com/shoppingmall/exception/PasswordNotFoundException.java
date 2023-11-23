package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class PasswordNotFoundException extends RuntimeException {

    public PasswordNotFoundException(String msg) {
        super(msg);
    }
}
