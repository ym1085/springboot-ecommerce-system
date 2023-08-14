package com.multi.utils;

import com.multi.posts.constant.StatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// ApiResponse<T> 싹 다 뜯어 고쳐야 할 듯
@Getter
@Setter
public class ApiResponse<T> {
    private int statusCode;
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

    public ApiResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = List.of(message);
    }
}
