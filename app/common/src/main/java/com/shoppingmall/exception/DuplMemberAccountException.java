package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class DuplMemberAccountException extends CustomException {

    public DuplMemberAccountException() {
        super(ErrorCode.FAIL_DUPL_MEMBER);
    }
}
