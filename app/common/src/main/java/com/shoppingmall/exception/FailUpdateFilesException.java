package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class FailUpdateFilesException extends CustomException {

    public FailUpdateFilesException() {
        super(ErrorCode.FAIL_UPLOAD_FILES);
    }
}
