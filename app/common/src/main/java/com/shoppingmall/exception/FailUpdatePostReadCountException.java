package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailUpdatePostReadCountException extends CustomException {

    public FailUpdatePostReadCountException() {
        super(ErrorCode.FAIL_UPDATE_POST_READ_COUNT);
    }
}
