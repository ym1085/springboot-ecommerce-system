package com.shoppingmall.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseResponse<T> {
    private final String statusCode; // HTTP status code
    private final boolean success; // success status
    private final String message; // response message

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final T result;

    public static <T> BaseResponse<?> of(String message, T data) {
        return BaseResponse.builder()
                .success(true)
                .message(message)
                .result(data)
                .build();
    }

    public static BaseResponse<?> of(boolean isSuccess, String message) {
        return BaseResponse.builder()
                .success(isSuccess)
                .message(message)
                .build();
    }

    public static BaseResponse<?> of(String statusCode, boolean isSuccess, String message) {
        return BaseResponse.builder()
                .statusCode(statusCode)
                .success(isSuccess)
                .message(message)
                .build();
    }
}
