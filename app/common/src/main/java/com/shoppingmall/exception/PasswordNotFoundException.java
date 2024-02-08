package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class PasswordNotFoundException extends CustomException {

    public PasswordNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
