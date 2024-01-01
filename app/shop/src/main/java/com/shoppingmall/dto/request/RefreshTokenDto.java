package com.shoppingmall.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenDto {

    @NotEmpty(message = "accessToken을 입력해주세요.")
    private String accessToken;

    @NotEmpty(message = "refreshToken을 입력해주세요.")
    private String refreshToken;

}
