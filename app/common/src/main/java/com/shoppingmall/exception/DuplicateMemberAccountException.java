package com.shoppingmall.exception;

import com.shoppingmall.common.error.MemberErrorCode;

public class DuplicateMemberAccountException extends CustomException {

    public DuplicateMemberAccountException() {
        super(MemberErrorCode.DUPLICATE_MEMBER_ACCOUNT);
    }
}
