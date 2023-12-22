package com.shoppingmall.mapper;

import com.shoppingmall.domain.RefreshToken;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface RefreshTokenMapper {

    Optional<RefreshToken> findByKey(String key);

}
