package com.shoppingmall.exception;

import com.shoppingmall.common.error.PostErrorCode;

public class FailDownloadFilesException extends CustomException {

    public FailDownloadFilesException() {
        super(PostErrorCode.FAIL_DOWNLOAD_FILES);
    }
}
