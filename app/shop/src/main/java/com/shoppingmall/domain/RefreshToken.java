package com.shoppingmall.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    private String key; // save memberId
    private String value; // save refresh token string

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}
