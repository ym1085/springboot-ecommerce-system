package com.shoppingmall.service;

import com.shoppingmall.dto.request.CartDeleteRequestDto;
import com.shoppingmall.dto.request.CartDetailRequestDto;
import com.shoppingmall.dto.request.CartSaveRequestDto;
import com.shoppingmall.dto.request.CartUpdateRequestDto;
import com.shoppingmall.dto.response.CartDetailResponseDto;
import com.shoppingmall.dto.response.CartUUIDResponseDto;
import com.shoppingmall.exception.CartException;
import com.shoppingmall.mapper.CartMapper;
import com.shoppingmall.mapper.MemberMapper;
import com.shoppingmall.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.shoppingmall.common.code.failure.cart.CartFailureCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartService {

    private final MemberMapper memberMapper;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    /**
     * 장바구니 등록 및 업데이트
     *
     * 'N' > 0 : 기존 장바구니 내역 존재하기에 장바구니 업데이트
     * 'N' == 0 : 기존 장바구니 내역 존재하지 않기에 장바구니 등록
     * 'N' < 0 : 예외 처리
     */
    @Transactional
    public void addOrUpdateCartProduct(CartSaveRequestDto cartSaveRequestDto) {
        if (!isExistsProduct(cartSaveRequestDto)) {
            throw new IllegalArgumentException("해당 상품이 존재하지 않습니다.");
        }

        // 해당 회원 장바구니에 등록된 상품이 있는지 확인
        int cartProductsCount = cartMapper.countCartProducts(cartSaveRequestDto.toEntity());

        if (cartProductsCount > 0) {
            updateCartProducts(cartSaveRequestDto);
        } else if (cartProductsCount == 0) {
            addCartProduct(cartSaveRequestDto);
        } else {
            // 0 미만은 오류로 판단 후 예외 발생!! -> 0 미만의 값이 DB의 장바구니 테이블에 들어가 있으면 안됨
            throw new RuntimeException("장바구니 등록 및 업데이트 중 오류가 발생했습니다.");
        }
    }

    private boolean isExistsProduct(CartSaveRequestDto cartRequestDto) {
        return productMapper.getProductByProductId(cartRequestDto.getProductId()).isPresent(); // 상품이 먼저 존재하는 지 한번 더 검증
    }

    private void updateCartProducts(CartSaveRequestDto cartSaveRequestDto) {
        if (cartMapper.updateCartProduct(cartSaveRequestDto.toEntity()) < 1) {
            throw new CartException(UPDATE_CART);
        }
    }

    private void addCartProduct(CartSaveRequestDto cartSaveRequestDto) {
        if (cartMapper.addCartProduct(cartSaveRequestDto.toEntity()) < 1) {
            throw new CartException(SAVE_CART);
        }
    }

    public List<CartDetailResponseDto> getCartItems(CartDetailRequestDto cartDetailRequestDto) {
        List<CartDetailResponseDto> cartDetailResponseDtos = cartMapper.getCartItems(cartDetailRequestDto.toEntity())
                .stream()
                .filter(Objects::nonNull)
                .map(CartDetailResponseDto::toDto)
                .collect(Collectors.toList());

        return cartDetailResponseDtos;
    }

    @Transactional
    public void updateCartProducts(CartUpdateRequestDto cartUpdateRequestDto) {
        if (cartMapper.updateCartProduct(cartUpdateRequestDto.toEntity()) < 1) {
            throw new CartException(UPDATE_CART);
        }
    }

    @Transactional
    public void deleteCartItem(CartDeleteRequestDto cartDeleteRequestDto) {
        if (cartMapper.deleteCartItem(cartDeleteRequestDto.toEntity()) < 1) {
            throw new CartException(DELETE_CART);
        }
    }

    private int getTotalPriceCartItems(Integer memberId) {
        return cartMapper.getCartItemsTotalPrice(memberId);
    }

    private CartUUIDResponseDto getCartUUIDResponseDto(String uuid) {
        return CartUUIDResponseDto.builder().uuid(uuid).build();
    }
}