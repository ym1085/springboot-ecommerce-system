package com.shoppingmall.api;

import com.shoppingmall.common.response.ApiUtils;
import com.shoppingmall.common.response.CommonResponse;
import com.shoppingmall.common.response.ErrorCode;
import com.shoppingmall.common.response.SuccessCode;
import com.shoppingmall.config.auth.PrincipalUserDetails;
import com.shoppingmall.dto.request.CartSaveRequestDto;
import com.shoppingmall.dto.request.CartUpdateRequestDto;
import com.shoppingmall.dto.response.CartDetailResponseDto;
import com.shoppingmall.dto.response.CartTotalPriceResponseDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.CartService;
import com.shoppingmall.utils.SecurityUtils;
import com.shoppingmall.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 좋은 API Response Body 만들기 : https://jojoldu.tistory.com/720

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class CartRestController {

    private final CartService cartService;

    @PostMapping("/cart")
    public ResponseEntity<CommonResponse> addCartItem(
            @RequestParam(value = "cartUUID", required = false) Integer cartUUID,
            @AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
            @RequestBody @Valid CartSaveRequestDto cartRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        Member member = principalUserDetails.getMember();

        if (!SecurityUtils.isValidLoginMember(member)) {
            // TODO: 비로그인 사용자인 경우
        }

        cartRequestDto.setMemberId(member.getMemberId());

        cartService.addCartProduct(cartRequestDto);

        return ApiUtils.success(SuccessCode.SAVE_CART.getCode(), SuccessCode.SAVE_CART.getHttpStatus(), SuccessCode.SAVE_CART.getMessage());
    }

    @GetMapping("/cart")
    public ResponseEntity<CommonResponse> getCartItems(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails) {
        Member member = principalUserDetails.getMember();

        if (!SecurityUtils.isValidLoginMember(member)) {
            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다. 다시 시도해주세요.");
        }

        // 회원 번호 기반 장바구니 목록 조회
        List<CartDetailResponseDto> cartItems = cartService.getCartItems(member.getMemberId());

        // 장바구니 정보 저장 및 장바구니 상품별 총 합계 저장
        CartTotalPriceResponseDto cartTotalPriceResponseDto = CartTotalPriceResponseDto.calculateTotalPrice(cartItems);

        return ApiUtils.success(SuccessCode.OK.getCode(), SuccessCode.OK.getHttpStatus(), SuccessCode.OK.getMessage(), cartTotalPriceResponseDto);
    }

    @PutMapping("/cart/update/{cartId}")
    public ResponseEntity<CommonResponse> updateCartItem(
            @AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
            @PathVariable("cartId") Integer cartId,
            @RequestBody @Valid CartUpdateRequestDto cartUpdateRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        Member member = principalUserDetails.getMember();

        if (!SecurityUtils.isValidLoginMember(member)) {
            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다. 다시 시도해주세요.");
        }

        cartUpdateRequestDto.setMemberId(member.getMemberId());

        cartService.updateCartProduct(cartUpdateRequestDto);

        return ApiUtils.success(SuccessCode.UPDATE_CART.getCode(), SuccessCode.UPDATE_CART.getHttpStatus(), SuccessCode.UPDATE_CART.getMessage());
    }

    @DeleteMapping("/cart/delete/{cartId}")
    public ResponseEntity<CommonResponse> deleteCartItem(
            @AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
            @PathVariable("cartId") Integer cartId) {

        if (cartId == null) {
            return ApiUtils.fail(ErrorCode.BAD_REQUEST.getCode(), ErrorCode.BAD_REQUEST.getHttpStatus(), ErrorCode.BAD_REQUEST.getMessage());
        }

        Member member = principalUserDetails.getMember();

        if (!SecurityUtils.isValidLoginMember(member)) {
            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다. 다시 시도해주세요.");
        }

        cartService.deleteCartItem(cartId, member.getMemberId());

        return ApiUtils.success(SuccessCode.DELETE_CART.getCode(), SuccessCode.DELETE_CART.getHttpStatus(), SuccessCode.DELETE_CART.getMessage());
    }
}
