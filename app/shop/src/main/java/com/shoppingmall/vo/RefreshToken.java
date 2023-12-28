package com.shoppingmall.vo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    private Long refreshTokenId;
    private Long memberId;
    private String refreshToken;
}
