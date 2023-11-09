package com.shoppingmall.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CommonResponse<T> {
    private int code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY) // NULL아니고 공백 아닌 경우
    private Map<String, String> errorMessage = new HashMap<>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY) // NULL아니고 공백 아닌 경우
    private T result;

    public CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResponse(int code, String message, Map<String, String> errorMessage) {
        this.code = code;
        this.message = message;
        this.errorMessage = errorMessage;
    }

    public CommonResponse(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
