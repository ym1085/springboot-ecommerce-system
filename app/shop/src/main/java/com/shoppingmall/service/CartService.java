package com.shoppingmall.service;

import com.shoppingmall.common.response.ApiUtils;
import com.shoppingmall.common.response.CommonResponse;
import com.shoppingmall.common.response.ErrorCode;
import com.shoppingmall.common.response.SuccessCode;
import com.shoppingmall.dto.request.CartDeleteRequestDto;
import com.shoppingmall.dto.request.CartDetailRequestDto;
import com.shoppingmall.dto.request.CartSaveRequestDto;
import com.shoppingmall.dto.request.CartUpdateRequestDto;
import com.shoppingmall.dto.response.CartDetailResponseDto;
import com.shoppingmall.dto.response.CartUUIDResponseDto;
import com.shoppingmall.exception.FailDeleteCartProductException;
import com.shoppingmall.exception.FailSaveCartProductException;
import com.shoppingmall.exception.FailUpdateCartProductException;
import com.shoppingmall.mapper.CartMapper;
import com.shoppingmall.mapper.MemberMapper;
import com.shoppingmall.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 장바구니 등록 및 업데이트
     *
     * 'N' > 0 : 기존 장바구니 내역 존재하기에 장바구니 업데이트
     * 'N' == 0 : 기존 장바구니 내역 존재하지 않기에 장바구니 등록
     * 'N' < 0 : 예외 처리
     */
    @Transactional
    public ResponseEntity<CommonResponse> addCartProductForMember(CartSaveRequestDto cartSaveRequestDto) {
        if (!isExistsProduct(cartSaveRequestDto)) {
            throw new IllegalArgumentException("해당 상품이 존재하지 않습니다.");
        }

        // 해당 회원 장바구니에 등록된 상품이 있는지 확인
        int cartProductsCount = cartMapper.countCartProducts(cartSaveRequestDto.toEntity());

        ResponseEntity<CommonResponse> response;
        if (cartProductsCount > 0) {
            response = updateCartProduct(cartSaveRequestDto);
        } else if (cartProductsCount == 0) {
            response = addCartProduct(cartSaveRequestDto);
        } else {
            // 0 미만은 오류로 판단 후 예외 발생!! -> 0 미만의 값이 DB의 장바구니 테이블에 들어가 있으면 안됨
            throw new RuntimeException("장바구니 등록 및 업데이트 중 오류가 발생했습니다.");
        }
        return response;
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
    public ResponseEntity<CommonResponse> updateCartProduct(CartUpdateRequestDto cartUpdateRequestDto) {
        if (cartMapper.updateCartProduct(cartUpdateRequestDto.toEntity()) < 1) {
            throw new FailUpdateCartProductException(ErrorCode.UPDATE_CART);
        }
        return buildSuccessResponse(SuccessCode.UPDATE_CART, cartUpdateRequestDto.getUuid());
    }

    @Transactional
    public ResponseEntity<CommonResponse> deleteCartItem(CartDeleteRequestDto cartDeleteRequestDto) {
        if (cartMapper.deleteCartItem(cartDeleteRequestDto.toEntity()) < 1) {
            throw new FailDeleteCartProductException(ErrorCode.DELETE_CART);
        }
        return buildSuccessResponse(SuccessCode.DELETE_CART, cartDeleteRequestDto.getUuid());
    }

    private int getTotalPriceCartItems(Integer memberId) {
        return cartMapper.getCartItemsTotalPrice(memberId);
    }

    private boolean isExistsProduct(CartSaveRequestDto cartRequestDto) {
        return productMapper.getProductByProductId(cartRequestDto.getProductId()).isPresent(); // 상품이 먼저 존재하는 지 한번 더 검증
    }

    private ResponseEntity<CommonResponse> updateCartProduct(CartSaveRequestDto cartSaveRequestDto) {
        if (cartMapper.updateCartProduct(cartSaveRequestDto.toEntity()) < 1) {
            throw new FailUpdateCartProductException(ErrorCode.UPDATE_CART);
        }
        log.info("[addCartProductForMember] UPDATE cart product success");
        return buildSuccessResponse(SuccessCode.UPDATE_CART, cartSaveRequestDto.getUuid());
    }

    private ResponseEntity<CommonResponse> addCartProduct(CartSaveRequestDto cartSaveRequestDto) {
        if (cartMapper.addCartProduct(cartSaveRequestDto.toEntity()) < 1) {
            throw new FailSaveCartProductException(ErrorCode.SAVE_CART);
        }
        log.info("[addCartProductForMember] INSERT cart product success");
        return buildSuccessResponse(SuccessCode.SAVE_CART, cartSaveRequestDto.getUuid());
    }

    private ResponseEntity<CommonResponse> buildSuccessResponse(SuccessCode successCode, String uuid) {
        return ApiUtils.success(
                successCode.getCode(),
                successCode.getHttpStatus(),
                successCode.getMessage(),
                StringUtils.isNotBlank(uuid) ? getCartUUIDResponseDto(uuid) : ""
        );
    }

    private CartUUIDResponseDto getCartUUIDResponseDto(String uuid) {
        return CartUUIDResponseDto.builder().uuid(uuid).build();
    }
}