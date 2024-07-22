package com.shoppingmall.exception;

import com.shoppingmall.common.code.failure.FailureCode;
import lombok.Getter;

@Getter
public class PostException extends RuntimeException {

    private final FailureCode failureCode;

    public PostException(FailureCode failureCode) {
        super("[PostException] : " + failureCode);
        this.failureCode = failureCode;
    }
}
