package com.post.dto.resposne;

import com.post.constant.StatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message<T> {
    private StatusEnum status;
    private T message;
    private Object data;

    public Message(StatusEnum status, T message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Message(StatusEnum status, T message) {
        this.status = status;
        this.message = message;
    }
}
