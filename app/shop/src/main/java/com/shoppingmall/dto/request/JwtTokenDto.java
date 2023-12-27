package com.shoppingmall.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class JwtTokenDto {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpirationTime;
    private String refreshToken;
    private Long refreshTokenExpirationTime;
}
