package com.shoppingmall.api;

import com.shoppingmall.common.response.ApiUtils;
import com.shoppingmall.common.response.CommonResponse;
import com.shoppingmall.common.response.SuccessCode;
import com.shoppingmall.config.auth.PrincipalUserDetails;
import com.shoppingmall.dto.request.CartSaveRequestDto;
import com.shoppingmall.dto.response.CartTotalPriceResponseDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.CartService;
import com.shoppingmall.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
            @RequestBody @Valid CartSaveRequestDto cartRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        Member member = principalUserDetails.getMember();
        cartRequestDto.setMemberId(member.getMemberId());

        cartService.addCartProduct(cartRequestDto);

        return ApiUtils.success(
                SuccessCode.SAVE_CART.getCode(),
                SuccessCode.SAVE_CART.getHttpStatus(),
                SuccessCode.SAVE_CART.getMessage()
        );
    }

    @GetMapping("/cart")
    public ResponseEntity<CommonResponse> getCartItems(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails) {
        Member member = principalUserDetails.getMember();

        CartTotalPriceResponseDto cartItems = cartService.getCartItems(member.getMemberId());

        return ApiUtils.success(
                SuccessCode.OK.getCode(),
                SuccessCode.OK.getHttpStatus(),
                SuccessCode.OK.getMessage(),
                cartItems
        );
    }

    @PutMapping("/cart/{cartId}")
    public ResponseEntity<CommonResponse> deleteCartItem(
            @AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
            @PathVariable("cartId") Integer cartId) {

        Member member = principalUserDetails.getMember();

        int responseCode = cartService.deleteCartItem(cartId, member.getMemberId());

        return ApiUtils.success(
                SuccessCode.DELETE_CART.getCode(),
                SuccessCode.DELETE_CART.getHttpStatus(),
                SuccessCode.DELETE_CART.getMessage()
        );
    }
}
