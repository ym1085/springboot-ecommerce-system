package com.shoppingmall.mapper;

import com.shoppingmall.vo.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    int countCartProducts(Cart cart);

    int addCartProduct(Cart cart);

    int updateCartProduct(Cart cart);

    List<Cart> getCartItems(Integer memberId);

    int getCartItemsTotalPrice(Integer memberId);

    int deleteCartItem(@Param("cartId") Integer cartId, @Param("memberId") Integer memberId);
}