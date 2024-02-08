package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailSaveFileException extends CustomException {

    public FailSaveFileException(ErrorCode errorCode) {
        super(errorCode);
    }
}
