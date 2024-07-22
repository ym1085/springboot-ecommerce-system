package com.shoppingmall.exception;

import com.shoppingmall.common.code.failure.FailureCode;
import lombok.Getter;

@Getter
public class CartException extends RuntimeException {

    private final FailureCode failureCode;

    public CartException(FailureCode failureCode) {
        super("[CartException]" + failureCode);
        this.failureCode = failureCode;
    }
}
