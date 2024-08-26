package com.shoppingmall.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDto {
    private String email;
    private String verifyCode;
}
