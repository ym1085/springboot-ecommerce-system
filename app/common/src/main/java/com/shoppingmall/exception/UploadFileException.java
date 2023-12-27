package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class UploadFileException extends CustomException {

    public UploadFileException() {
        super(ErrorCode.FAIL_UPDATE_FILES);
    }
}
