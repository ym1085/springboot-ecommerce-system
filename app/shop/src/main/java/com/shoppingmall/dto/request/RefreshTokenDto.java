package com.shoppingmall.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class RefreshTokenDto {

    @NotEmpty(message = "accessToken을 입력해주세요.")
    private String accessToken;

    @NotEmpty(message = "refreshToken을 입력해주세요.")
    private String refreshToken;
}
