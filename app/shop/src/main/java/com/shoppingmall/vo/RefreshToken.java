package com.shoppingmall.vo;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    private Long refreshTokenId;
    private Long memberId;
    private String refreshToken;

    public RefreshToken(Long memberId, String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }
}
