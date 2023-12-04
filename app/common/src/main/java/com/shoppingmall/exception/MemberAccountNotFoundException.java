package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class MemberAccountNotFoundException extends CustomException {

    private static final long serialVersionUID = -2116671122895194101L;

    public MemberAccountNotFoundException() {
        super(ErrorCode.FAIL_DOWNLOAD_FILES);
    }
}
