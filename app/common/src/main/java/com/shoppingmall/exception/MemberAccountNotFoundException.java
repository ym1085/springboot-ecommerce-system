package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class MemberAccountNotFoundException extends CustomException {

    public MemberAccountNotFoundException() {
        super(ErrorCode.FAIL_DOWNLOAD_FILES);
    }
}
