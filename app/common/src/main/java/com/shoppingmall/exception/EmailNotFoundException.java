package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class EmailNotFoundException extends CustomException {

    public EmailNotFoundException() {
        super(ErrorCode.NOT_FOUND_MEMBER_EMAIL);
    }
}
