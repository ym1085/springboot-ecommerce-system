package com.shoppingmall.api;

import com.shoppingmall.common.response.ApiUtils;
import com.shoppingmall.common.response.CommonResponse;
import com.shoppingmall.common.response.ErrorCode;
import com.shoppingmall.common.response.SuccessCode;
import com.shoppingmall.dto.request.CartSaveRequestDto;
import com.shoppingmall.dto.response.CartTotalPriceResponseDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.CartService;
import com.shoppingmall.utils.SecurityUtils;
import com.shoppingmall.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class CartRestController {

    private final CartService cartService;

    @PostMapping("/cart")
    public ResponseEntity<CommonResponse> addCartItem(
            @RequestBody @Valid CartSaveRequestDto cartRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        Member member = SecurityUtils.getCurrentMember()
                .orElseThrow(() -> new AccessDeniedException("인증된 사용자 정보를 찾을 수 없습니다."));

        cartRequestDto.setMemberId(member.getMemberId());
        log.debug("member = {}, cartRequestDto = {}", member, cartRequestDto);

        int responseCode = cartService.addCartProduct(cartRequestDto);

        if (responseCode == 1) {
            return ApiUtils.success(
                    SuccessCode.UPDATE_CART.getCode(),
                    SuccessCode.UPDATE_CART.getHttpStatus(),
                    SuccessCode.UPDATE_CART.getMessage()
            );
        } else if (responseCode == 2) {
            return ApiUtils.success(
                    SuccessCode.SAVE_CART.getCode(),
                    SuccessCode.SAVE_CART.getHttpStatus(),
                    SuccessCode.SAVE_CART.getMessage()
            );
        } else {
            return ApiUtils.fail(
                    ErrorCode.SAVE_CART.getCode(),
                    ErrorCode.SAVE_CART.getHttpStatus(),
                    ErrorCode.SAVE_CART.getMessage()
            );
        }
    }

    @GetMapping("/cart")
    public ResponseEntity<CommonResponse> getCartItems() {
        Member member = SecurityUtils.getCurrentMember()
                .orElseThrow(() -> new AccessDeniedException("인증된 사용자 정보를 찾을 수 없습니다."));

        CartTotalPriceResponseDto cartItems = cartService.getCartItems(member.getMemberId());

        return ApiUtils.success(
                SuccessCode.OK.getCode(),
                SuccessCode.OK.getHttpStatus(),
                SuccessCode.OK.getMessage(),
                cartItems
        );
    }

    @PutMapping("/cart/{cartId}")
    public ResponseEntity<CommonResponse> deleteCartItem(@PathVariable("cartId") Long cartId) {
        Member member = SecurityUtils.getCurrentMember()
                .orElseThrow(() -> new AccessDeniedException("인증된 사용자 정보를 찾을 수 없습니다."));

        int responseCode = cartService.deleteCartItem(cartId, member.getMemberId());

        return ApiUtils.success(
                SuccessCode.DELETE_CART.getCode(),
                SuccessCode.DELETE_CART.getHttpStatus(),
                SuccessCode.DELETE_CART.getMessage()
        );
    }
}
