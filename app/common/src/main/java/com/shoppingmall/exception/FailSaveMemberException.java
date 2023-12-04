package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailSaveMemberException extends CustomException {

    private static final long serialVersionUID = -2116671122895194101L;

    public FailSaveMemberException() {
        super(ErrorCode.FAIL_SAVE_MEMBER);
    }
}
