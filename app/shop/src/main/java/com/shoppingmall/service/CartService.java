package com.shoppingmall.service;

import com.shoppingmall.dto.request.CartDeleteRequestDto;
import com.shoppingmall.dto.request.CartDetailRequestDto;
import com.shoppingmall.dto.request.CartUpdateRequestDto;
import com.shoppingmall.vo.CartTotalPrice;
import com.shoppingmall.exception.CartException;
import com.shoppingmall.mapper.CartMapper;
import com.shoppingmall.mapper.ProductMapper;
import com.shoppingmall.vo.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.shoppingmall.common.code.failure.cart.CartFailureCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartService {
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
    public void addOrUpdateCartProduct(CartUpdateRequestDto cartUpdateRequestDto) {
        if (!isExistsProduct(cartUpdateRequestDto)) {
            throw new IllegalArgumentException("해당 상품이 존재하지 않습니다.");
        }
        // 해당 회원 장바구니에 등록된 상품이 있는지 확인
        int cartProductsCount = cartMapper.countCartProducts(cartUpdateRequestDto);

        if (cartProductsCount > 0) {
            updateCartProducts(cartUpdateRequestDto);
        } else if (cartProductsCount == 0) {
            addCartProduct(cartUpdateRequestDto);
        } else {
            // 0 미만은 오류로 판단 후 예외 발생!! -> 0 미만의 값이 DB의 장바구니 테이블에 들어가 있으면 안됨
            throw new RuntimeException("장바구니 등록 및 업데이트 중 오류가 발생했습니다.");
        }
    }

    private boolean isExistsProduct(CartUpdateRequestDto cartUpdateRequestDto) {
        return productMapper.getProductByProductId(cartUpdateRequestDto.getProductId()).isPresent(); // 상품이 먼저 존재하는 지 한번 더 검증
    }

    private void addCartProduct(CartUpdateRequestDto cartUpdateRequestDto) {
        if (cartMapper.addCartProduct(cartUpdateRequestDto) < 1) {
            throw new CartException(FAIL_SAVE_CART);
        }
    }

    public CartTotalPrice getCartItems(CartDetailRequestDto cartDetailRequestDto) {
        List<Cart> cartItems = cartMapper.getCartItems(cartDetailRequestDto);
        CartTotalPrice cartTotalPrice = new CartTotalPrice(); // 장바구니 정보 저장 및 장바구니 상품별 총 합계 저장
        if (!CollectionUtils.isEmpty(cartItems)) {
            cartTotalPrice = CartTotalPrice.calculateTotalPrice(cartItems);
        }
        return cartTotalPrice;
    }

    @Transactional
    public void updateCartProducts(CartUpdateRequestDto cartUpdateRequestDto) {
        if (cartMapper.updateCartProduct(cartUpdateRequestDto) < 1) {
            throw new CartException(FAIL_UPDATE_CART);
        }
    }

    @Transactional
    public void deleteCartItem(CartDeleteRequestDto cartDeleteRequestDto) {
        if (cartMapper.deleteCartItem(cartDeleteRequestDto) < 1) {
            throw new CartException(FAIL_DELETE_CART);
        }
    }
}