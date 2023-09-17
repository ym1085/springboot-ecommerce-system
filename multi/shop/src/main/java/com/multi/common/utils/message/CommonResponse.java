package com.multi.common.utils.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommonResponse {
    private int code;
    private List<?> data;

    public CommonResponse(int code, List<?> data) {
        this.code = code;
        this.data = data;
    }
}
