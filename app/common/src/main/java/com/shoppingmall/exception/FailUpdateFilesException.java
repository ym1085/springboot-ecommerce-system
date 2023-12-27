package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailUpdateFilesException extends CustomException {

    public FailUpdateFilesException() {
        super(ErrorCode.FAIL_UPDATE_FILES);
    }
}
