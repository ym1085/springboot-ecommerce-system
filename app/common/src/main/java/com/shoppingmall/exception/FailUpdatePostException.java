package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailUpdatePostException extends CustomException {

    public FailUpdatePostException() {
        super(ErrorCode.FAIL_UPDATE_POST);
    }
}
