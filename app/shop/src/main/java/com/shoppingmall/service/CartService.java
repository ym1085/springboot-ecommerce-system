package com.shoppingmall.service;

import com.shoppingmall.common.response.ErrorCode;
import com.shoppingmall.dto.request.CartSaveRequestDto;
import com.shoppingmall.dto.request.CartUpdateRequestDto;
import com.shoppingmall.dto.response.CartDetailResponseDto;
import com.shoppingmall.exception.FailDeleteCartProductException;
import com.shoppingmall.exception.FailSaveCartProductException;
import com.shoppingmall.exception.FailUpdateCartProductException;
import com.shoppingmall.mapper.CartMapper;
import com.shoppingmall.mapper.MemberMapper;
import com.shoppingmall.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartService {

    private final MemberMapper memberMapper;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    @Transactional
    public void addCartProductForMember(CartSaveRequestDto cartRequestDto) {
        if (!isExistsProduct(cartRequestDto)) {
            throw new IllegalArgumentException("해당 상품이 존재하지 않습니다.");
        }

        // 해당 회원 장바구니에 등록된 상품이 있는지 확인
        int cartProductsCount = cartMapper.countCartProducts(cartRequestDto.toEntity());
        if (cartProductsCount > 0) {
            if (cartMapper.updateCartProduct(cartRequestDto.toEntity()) < 1) {
                throw new FailUpdateCartProductException(ErrorCode.UPDATE_CART);
            }
        } else {
            if (cartMapper.addCartProduct(cartRequestDto.toEntity()) < 1) {
                throw new FailSaveCartProductException(ErrorCode.SAVE_CART);
            }
        }
    }

    @Transactional
    public void updateCartProduct(CartUpdateRequestDto cartUpdateRequestDto) {
        int responseCode = cartMapper.updateCartProduct(cartUpdateRequestDto.toEntity());
        if (responseCode == 0) {
            throw new FailUpdateCartProductException(ErrorCode.UPDATE_CART);
        }
    }

    public List<CartDetailResponseDto> getCartItems(Integer memberId) {
        if (memberId == null) {
            return Collections.emptyList();
        }

        return cartMapper.getCartItems(memberId)
                .stream()
                .filter(Objects::nonNull)
                .map(CartDetailResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCartItem(Integer cartId, Integer memberId) {
        int responseCode = cartMapper.deleteCartItem(cartId, memberId);
        if (responseCode == 0) {
            throw new FailDeleteCartProductException(ErrorCode.DELETE_CART);
        }
    }

    private int getTotalPriceCartItems(Integer memberId) {
        return cartMapper.getCartItemsTotalPrice(memberId);
    }

    private boolean isExistsProduct(CartSaveRequestDto cartRequestDto) {
        return productMapper.getProductByProductId(cartRequestDto.getProductId()).isPresent(); // 상품이 먼저 존재하는 지 한번 더 검증
    }
}