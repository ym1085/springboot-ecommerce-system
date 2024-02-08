package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailAuthenticationMemberEmailException extends CustomException {

    public FailAuthenticationMemberEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
