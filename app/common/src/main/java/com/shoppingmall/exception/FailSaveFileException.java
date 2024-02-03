package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailSaveFileException extends CustomException {

    public FailSaveFileException() {
        super(ErrorCode.FAIL_SAVE_FILES);
    }
}
