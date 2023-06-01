package com.multi.utils;

import com.multi.posts.constant.StatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private StatusEnum status;
    private String message;
    private T data;

    public ApiResponse(StatusEnum status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(StatusEnum status, String message) {
        this.status = status;
        this.message = message;
    }
}
