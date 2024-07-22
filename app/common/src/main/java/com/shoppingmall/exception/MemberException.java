package com.shoppingmall.exception;

import com.shoppingmall.common.code.failure.FailureCode;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {

    private final FailureCode failureCode;

    public MemberException(FailureCode failureCode) {
        super("[MemberException] : " + failureCode);
        this.failureCode = failureCode;
    }
}
