package com.shoppingmall.utils;

import com.shoppingmall.common.ErrorCode;

public class ResponseUtils {

    public static boolean isSuccessResponseCode(int responseCode) {
        return responseCode > ErrorCode.FAIL.getCode();
    }
}
