package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailDeleteCommentException extends CustomException {

    public FailDeleteCommentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
