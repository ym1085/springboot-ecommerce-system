package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;

public class DuplicateMemberAccountException extends CustomException {

    public DuplicateMemberAccountException() {
        super(ErrorCode.DUPLICATE_MEMBER_ACCOUNT);
    }
}
