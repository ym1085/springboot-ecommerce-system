package com.shoppingmall.dto.request;

import lombok.*;

@Getter
@Setter
public class EmailRequestDto {
    private String email;
    private String authCode;
}
