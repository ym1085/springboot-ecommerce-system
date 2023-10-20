package com.shoppingmall.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EmailRequestDto {
    private String email;
    private String authCode;
}
