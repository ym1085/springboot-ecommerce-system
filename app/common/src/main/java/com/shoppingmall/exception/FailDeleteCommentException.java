package com.shoppingmall.exception;

import com.shoppingmall.common.error.PostErrorCode;

public class FailDeleteCommentException extends CustomException {

    public FailDeleteCommentException() {
        super(PostErrorCode.FAIL_DELETE_COMMENT);
    }
}
