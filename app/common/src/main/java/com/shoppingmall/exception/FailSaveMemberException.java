package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailSaveMemberException extends CustomException {

    public FailSaveMemberException() {
        super(ErrorCode.FAIL_SAVE_MEMBER);
    }
}
