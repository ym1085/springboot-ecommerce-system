package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailUpdateFilesException extends CustomException {

    private static final long serialVersionUID = -2116671122895194101L;

    public FailUpdateFilesException() {
        super(ErrorCode.FAIL_UPDATE_FILES);
    }
}
