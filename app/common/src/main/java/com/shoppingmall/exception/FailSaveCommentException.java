package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailSaveCommentException extends CustomException {

    public FailSaveCommentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
