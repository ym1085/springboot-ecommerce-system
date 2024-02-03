package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;
import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class InvalidParameterException extends CustomException {

    private final Errors errors;

    public InvalidParameterException(Errors errors) {
        super(ErrorCode.BAD_REQUEST);
        this.errors = errors;
    }
}
