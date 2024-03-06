package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailUpdateProductException extends CustomException {

    public FailUpdateProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}
