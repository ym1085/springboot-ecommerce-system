package com.shoppingmall.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefreshTokenDto {

    private String accessToken;
    private String refreshToken;

}
