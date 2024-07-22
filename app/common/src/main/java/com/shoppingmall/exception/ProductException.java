package com.shoppingmall.exception;

import com.shoppingmall.common.code.failure.FailureCode;
import lombok.Getter;

@Getter
public class ProductException extends RuntimeException {

    private final FailureCode failureCode;

    public ProductException(FailureCode failureCode) {
        super("[ProductException] : " + failureCode);
        this.failureCode = failureCode;
    }
}
