package com.shoppingmall.service;

import com.shoppingmall.mapper.RefreshTokenMapper;
import com.shoppingmall.vo.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RefreshTokenService {

    private final RefreshTokenMapper refreshTokenMapper;

    @Transactional
    public int addRefreshToken(RefreshToken refreshToken) {
        return refreshTokenMapper.saveRefreshToken(refreshToken);
    }

    @Transactional
    public int deleteRefreshToken(String refreshToken) {
        return refreshTokenMapper.deleteRefreshToken(refreshToken);
    }

    public Optional<RefreshToken> findRefreshToken(String refreshToken) {
        return refreshTokenMapper.findByRefreshToken(refreshToken);
    }
}
