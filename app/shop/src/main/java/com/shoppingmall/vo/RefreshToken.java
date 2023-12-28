package com.shoppingmall.vo;

import lombok.*;

@Getter
@NoArgsConstructor
public class RefreshToken {
    private Long refreshTokenId;
    private Long memberId;
    private String refreshToken;

    public RefreshToken(Long refreshTokenId, Long memberId, String refreshToken) {
        this.refreshTokenId = refreshTokenId;
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    @Builder
    public RefreshToken(Long memberId, String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }
}
