package com.shoppingmall.exception;

import com.shoppingmall.common.code.failure.FailureCode;
import lombok.Getter;

@Getter
public class FileException extends RuntimeException {

    private final FailureCode failureCode;

    public FileException(FailureCode failureCode) {
        super("[FileException] : " + failureCode);
        this.failureCode = failureCode;
    }
}
