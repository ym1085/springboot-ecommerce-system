package com.multi.utils;

import com.multi.posts.constant.StatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiResponse<T> {
    private StatusEnum status;
    private List<String> message;
    private T data;

    public ApiResponse(StatusEnum status, String message, T data) {
        this.status = status;
        this.message = List.of(message);
        this.data = data;
    }

    public ApiResponse(StatusEnum status, List<String> message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(StatusEnum status, String message) {
        this.status = status;
        this.message = List.of(message);
    }

    public ApiResponse(StatusEnum status, List<String> message) {
        this.status = status;
        this.message = message;
    }
}
