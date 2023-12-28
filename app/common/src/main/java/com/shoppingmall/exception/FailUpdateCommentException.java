package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailUpdateCommentException extends CustomException {

    public FailUpdateCommentException() {
        super(ErrorCode.FAIL_UPDATE_COMMENT);
    }
}