package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class DuplMemberAccountException extends CustomException {

    private static final long serialVersionUID = -2116671122895194101L;

    public DuplMemberAccountException() {
        super(ErrorCode.FAIL_DUPL_MEMBER);
    }
}
