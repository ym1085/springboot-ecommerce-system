package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailDownloadFilesException extends CustomException {

    private static final long serialVersionUID = -2116671122895194101L;

    public FailDownloadFilesException() {
        super(ErrorCode.FAIL_DOWNLOAD_FILES);
    }
}
