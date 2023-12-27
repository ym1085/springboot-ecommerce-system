package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailDownloadFilesException extends CustomException {

    public FailDownloadFilesException() {
        super(ErrorCode.FAIL_DOWNLOAD_FILES);
    }
}
