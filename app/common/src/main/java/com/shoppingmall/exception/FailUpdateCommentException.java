package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailUpdateCommentException extends CustomException {

    private static final long serialVersionUID = -2116671122895194101L;

    public FailUpdateCommentException() {
        super(ErrorCode.FAIL_UPDATE_COMMENT);
    }
}
