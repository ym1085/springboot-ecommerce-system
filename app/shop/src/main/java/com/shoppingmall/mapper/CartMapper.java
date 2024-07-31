package com.shoppingmall.mapper;

import com.shoppingmall.dto.request.CartDeleteRequestDto;
import com.shoppingmall.dto.request.CartDetailRequestDto;
import com.shoppingmall.dto.request.CartUpdateRequestDto;
import com.shoppingmall.vo.Cart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

    int countCartProducts(CartUpdateRequestDto cartUpdateRequestDto);

    int addCartProduct(CartUpdateRequestDto cartUpdateRequestDto);

    int updateCartProduct(CartUpdateRequestDto cartUpdateRequestDto);

    List<Cart> getCartItems(CartDetailRequestDto cartDetailRequestDto);

    int getCartItemsTotalPrice(Integer memberId);

    int deleteCartItem(CartDeleteRequestDto cartDeleteRequestDto);
}