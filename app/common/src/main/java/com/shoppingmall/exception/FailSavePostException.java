package com.shoppingmall.exception;

import com.shoppingmall.common.ErrorCode;

public class FailSavePostException extends CustomException {

    private static final long serialVersionUID = -2116671122895194101L;

    public FailSavePostException() {
        super(ErrorCode.FAIL_SAVE_POST);
    }
}
