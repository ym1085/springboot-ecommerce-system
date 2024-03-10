package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailUpdateCartProductException extends CustomException {

    public FailUpdateCartProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}
