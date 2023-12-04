package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailSaveCommentException extends CustomException {

    private static final long serialVersionUID = -2116671122895194101L;

    public FailSaveCommentException() {
        super(ErrorCode.FAIL_SAVE_COMMENT);
    }
}
