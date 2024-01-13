package com.shoppingmall.exception;

import com.shoppingmall.common.error.PostErrorCode;

public class FailUpdateCommentException extends CustomException {

    public FailUpdateCommentException() {
        super(PostErrorCode.FAIL_UPDATE_COMMENT);
    }
}
