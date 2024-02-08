package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class DuplicateMemberAccountException extends CustomException {

    public DuplicateMemberAccountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
