package com.shoppingmall.vo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    private Integer refreshTokenId;
    private Integer memberId;
    private String refreshToken;
}
