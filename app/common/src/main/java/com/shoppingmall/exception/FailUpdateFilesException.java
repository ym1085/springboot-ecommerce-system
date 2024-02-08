package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailUpdateFilesException extends CustomException {

    public FailUpdateFilesException(ErrorCode errorCode) {
        super(errorCode);
    }
}
