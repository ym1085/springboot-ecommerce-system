package com.shoppingmall.exception;

import com.shoppingmall.common.error.PostErrorCode;

public class FailSavePostException extends CustomException {

    public FailSavePostException() {
        super(PostErrorCode.FAIL_SAVE_POST);
    }
}
