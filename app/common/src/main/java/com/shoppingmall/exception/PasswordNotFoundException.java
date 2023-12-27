package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class PasswordNotFoundException extends CustomException {

    public PasswordNotFoundException() {
        super(ErrorCode.NOT_FOUND_MEMBER_PASSWORD);
    }
}
