package com.shoppingmall.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CommonResponse<T> {
    private int code;
    private String message;
    private Map<String, String> errorMessage = new HashMap<>();
    private Result result = new Result();

    public CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResponse(int code, String message, Map<String, String> errorMessage) {
        this.code = code;
        this.message = message;
        this.errorMessage = errorMessage;
    }

    public CommonResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.result = new Result(data);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Result {
        private T data;

        public Result(T data) {
            this.data = data;
        }
    }
}
