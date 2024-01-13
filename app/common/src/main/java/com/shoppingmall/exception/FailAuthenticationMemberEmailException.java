package com.shoppingmall.exception;

import com.shoppingmall.common.error.MemberErrorCode;

public class FailAuthenticationMemberEmailException extends CustomException {

    public FailAuthenticationMemberEmailException() {
        super(MemberErrorCode.FAIL_AUTHENTICATION_MEMBER_EMAIL);
    }
}
