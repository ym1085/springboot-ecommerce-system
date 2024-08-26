package com.shoppingmall.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

import java.util.stream.Collectors;

import static com.shoppingmall.common.code.failure.CommonFailureCode.BAD_REQUEST;

@Getter
public class InvalidParameterException extends RuntimeException {

    private final Errors errors;

    public InvalidParameterException(Errors errors) {
        super("[InvalidParameterException] : " + BAD_REQUEST + " " + extractFieldErrorMessages(errors));
        this.errors = errors;
    }

    private static String extractFieldErrorMessages(Errors errors) {
        return errors.getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}
