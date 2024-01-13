package com.shoppingmall.exception;

import com.shoppingmall.common.error.MemberErrorCode;

public class PasswordNotFoundException extends CustomException {

    public PasswordNotFoundException() {
        super(MemberErrorCode.NOT_FOUND_MEMBER_PWD);
    }
}
