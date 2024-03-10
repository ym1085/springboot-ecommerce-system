package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailSaveCartProductException extends CustomException {

    public FailSaveCartProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}
