package com.shoppingmall.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommonResponse<T> {
    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final T result;

    public CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.result = null;
    }

    public CommonResponse(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
