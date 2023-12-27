package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailSaveCommentException extends CustomException {

    public FailSaveCommentException() {
        super(ErrorCode.FAIL_SAVE_COMMENT);
    }
}
