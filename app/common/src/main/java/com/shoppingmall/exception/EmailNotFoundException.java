package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class EmailNotFoundException extends CustomException {

    private static final long serialVersionUID = -2116671122895194101L;

    public EmailNotFoundException() {
        super(ErrorCode.NOT_FOUND_MEMBER_EMAIL);
    }
}
