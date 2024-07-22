package com.shoppingmall.exception;

import com.shoppingmall.common.code.failure.FailureCode;
import lombok.Getter;

@Getter
public class CommentException extends RuntimeException {

    private final FailureCode failureCode;

    public CommentException(FailureCode failureCode) {
        super("[CommentException] : " + failureCode);
        this.failureCode = failureCode;
    }
}
