package com.shoppingmall.exception;

import com.shoppingmall.common.error.PostErrorCode;

public class FailUpdateFilesException extends CustomException {

    public FailUpdateFilesException() {
        super(PostErrorCode.FAIL_UPLOAD_FILES);
    }
}
