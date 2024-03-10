package com.shoppingmall.mapper;

import com.shoppingmall.vo.Cart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper {

    int countCartProducts(Cart entity);

    int addCartProduct(Cart entity);

    int updateCartProduct(Cart entity);
}
