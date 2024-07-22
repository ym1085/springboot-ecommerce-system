package com.shoppingmall.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseResponse<T> {
    //private final String code; // 내부 커스텀 코드 번호 = [E0101, E0102..]
    private final boolean success;
    private final String message; // 반환 메시지

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
}
