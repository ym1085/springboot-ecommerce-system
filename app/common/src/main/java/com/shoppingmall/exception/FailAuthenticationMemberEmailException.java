package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailAuthenticationMemberEmailException extends CustomException {

    public FailAuthenticationMemberEmailException() {
        super(ErrorCode.FAIL_AUTHENTICATION_MEMBER_EMAIL);
    }
}
