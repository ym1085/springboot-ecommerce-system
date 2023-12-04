package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailSaveFileException extends CustomException {

    private static final long serialVersionUID = -2116671122895194101L;

    public FailSaveFileException() {
        super(ErrorCode.FAIL_SAVE_FILES);
    }
}
