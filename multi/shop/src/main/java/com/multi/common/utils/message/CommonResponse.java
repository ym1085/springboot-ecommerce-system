package com.multi.common.utils.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommonResponse {
    private MessageCode status;
    private List<?> data;

    public CommonResponse(MessageCode status, List<?> data) {
        this.status = status;
        this.data = data;
    }
}
