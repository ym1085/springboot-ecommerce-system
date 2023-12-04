package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class UploadFileException extends CustomException {

    private static final long serialVersionUID = -2116671122895194101L;

    public UploadFileException() {
        super(ErrorCode.FAIL_UPDATE_FILES);
    }
}
