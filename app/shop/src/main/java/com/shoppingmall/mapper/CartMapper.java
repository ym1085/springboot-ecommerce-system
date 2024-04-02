package com.shoppingmall.mapper;

import com.shoppingmall.vo.Cart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

    int countCartProducts(Cart cart);

    int addCartProduct(Cart cart);

    int updateCartProduct(Cart cart);

    List<Cart> getCartItems(Cart cart);

    int getCartItemsTotalPrice(Integer memberId);

    int deleteCartItem(Cart cart);
}