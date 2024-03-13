package com.shoppingmall.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse<T> {
    private final String code; // 내부 커스텀 코드 번호 = [E0101, E0102..]
    private final String message; // 반환 메시지

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final T result;

    public CommonResponse(String code, String message) {
        this.code = code;
        this.httpStatusCode = httpStatusCode;
        this.message = message;
        this.result = null;
    }

    public CommonResponse(String code, String message, T result) {
        this.code = code;
        this.httpStatusCode = httpStatusCode;
        this.message = message;
        this.result = result;
    }
}
