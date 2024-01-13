package com.shoppingmall.exception;

import com.shoppingmall.common.error.MemberErrorCode;

public class FailSaveMemberException extends CustomException {

    public FailSaveMemberException() {
        super(MemberErrorCode.FAIL_SAVE_MEMBER);
    }
}
