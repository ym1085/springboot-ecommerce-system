package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailDeleteCartProductException extends CustomException {

    public FailDeleteCartProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}
