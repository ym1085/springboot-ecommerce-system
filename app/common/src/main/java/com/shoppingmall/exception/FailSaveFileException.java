package com.shoppingmall.exception;

import com.shoppingmall.common.error.PostErrorCode;

public class FailSaveFileException extends CustomException {

    public FailSaveFileException() {
        super(PostErrorCode.FAIL_SAVE_FILES);
    }
}
