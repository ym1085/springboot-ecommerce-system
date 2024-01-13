package com.shoppingmall.exception;

import com.shoppingmall.common.error.CommonErrorCode;
import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class InvalidParameterException extends CustomException {

    private final Errors errors;

    public InvalidParameterException(Errors errors) {
        super(CommonErrorCode.INVALID_PARAMETER);
        this.errors = errors;
    }
}
