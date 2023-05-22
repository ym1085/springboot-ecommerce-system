package com.post.constant;

/**
 * 추후 ERROR CODE별로 만들어서 처리하면 될 듯
 * 지금은 큰 틀에서 (1: 성공, 0: 실패) 로만 구분 해두었음
 */
public enum ResponseCode {
    SUCCESS(1),
    FAIL(0)
    ;

    private final int responseCode;

    ResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
