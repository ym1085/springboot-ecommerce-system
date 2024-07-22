package com.shoppingmall.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

import static com.shoppingmall.common.code.failure.CommonFailureCode.BAD_REQUEST;

@Getter
public class InvalidParameterException extends RuntimeException {

    private final Errors errors;

    public InvalidParameterException(Errors errors) {
        super("[InvalidParameterException] : " + BAD_REQUEST);
        this.errors = errors;
    }
}
