package com.shoppingmall.mapper;

import com.shoppingmall.vo.RefreshToken;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface RefreshTokenMapper {

    int saveRefreshToken(RefreshToken refreshToken);

    int deleteRefreshToken(String refreshToken);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
