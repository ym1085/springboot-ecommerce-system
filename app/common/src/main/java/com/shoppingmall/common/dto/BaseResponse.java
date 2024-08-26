package com.shoppingmall.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonPropertyOrder(value = {"timestamp", "statusCode", "success", "message", "result"})
public class BaseResponse<T> {
    private final LocalDateTime timestamp;
    private final int statusCode;
    private final boolean success;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final T result;

    public static <T> BaseResponse<?> of(int statusCode, String message, T data) {
        return BaseResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(statusCode)
                .success(true)
                .message(message)
                .result(data)
                .build();
    }

    public static BaseResponse<?> of(int statusCode, String message, boolean isSuccess) {
        return BaseResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(statusCode)
                .success(isSuccess)
                .message(message)
                .build();
    }
}
