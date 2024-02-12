package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailSaveProductException extends CustomException {

    public FailSaveProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}
