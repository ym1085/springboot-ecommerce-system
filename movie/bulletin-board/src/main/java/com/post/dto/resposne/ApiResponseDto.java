package com.post.dto.resposne;

import com.post.constant.StatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseDto<T> {
    private StatusEnum status;
    private String message;
    private T data;

    public ApiResponseDto(StatusEnum status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponseDto(StatusEnum status, String message) {
        this.status = status;
        this.message = message;
    }
}
