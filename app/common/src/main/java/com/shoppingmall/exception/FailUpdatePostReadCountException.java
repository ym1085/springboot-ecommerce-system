package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailUpdatePostReadCountException extends CustomException {

    private static final long serialVersionUID = -2116671122895194101L;

    public FailUpdatePostReadCountException() {
        super(ErrorCode.FAIL_UPDATE_POST_READ_COUNT);
    }
}
