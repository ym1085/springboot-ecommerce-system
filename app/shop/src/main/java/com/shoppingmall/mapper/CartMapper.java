package com.shoppingmall.mapper;

import com.shoppingmall.vo.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    int countCartProducts(Cart entity);

    int addCartProduct(Cart entity);

    int updateCartProduct(Cart entity);

    List<Cart> getCartItems(Long memberId);

    int getCartItemsTotalPrice(Long memberId);

    int deleteCartItem(@Param("cartId") Long cartId, @Param("memberId") Long memberId);
}