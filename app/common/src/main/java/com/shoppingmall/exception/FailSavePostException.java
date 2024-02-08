package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailSavePostException extends CustomException {

    public FailSavePostException(ErrorCode errorCode) {
        super(errorCode);
    }
}
