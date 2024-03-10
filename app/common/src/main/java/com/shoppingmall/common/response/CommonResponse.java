package com.shoppingmall.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse<T> {
    private final int code; // custom status code
    private final int httpStatusCode; // HTTP status code
    private final String message; // response message

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final T result;

    public CommonResponse(int code, int httpStatusCode, String message) {
        this.code = code;
        this.httpStatusCode = httpStatusCode;
        this.message = message;
        this.result = null;
    }

    public CommonResponse(int code, int httpStatusCode, String message, T result) {
        this.code = code;
        this.httpStatusCode = httpStatusCode;
        this.message = message;
        this.result = result;
    }
}
