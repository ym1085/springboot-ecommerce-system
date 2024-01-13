package com.shoppingmall.exception;

import com.shoppingmall.common.error.PostErrorCode;

public class FailSaveCommentException extends CustomException {

    public FailSaveCommentException() {
        super(PostErrorCode.FAIL_SAVE_COMMENT);
    }
}
