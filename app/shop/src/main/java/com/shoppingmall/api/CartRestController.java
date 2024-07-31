package com.shoppingmall.api;

import com.shoppingmall.common.code.failure.CommonFailureCode;
import com.shoppingmall.common.code.success.cart.CartSuccessCode;
import com.shoppingmall.common.dto.BaseResponse;
import com.shoppingmall.common.utils.ApiResponseUtils;
import com.shoppingmall.common.utils.CommonUtils;
import com.shoppingmall.config.auth.PrincipalUserDetails;
import com.shoppingmall.dto.request.CartDeleteRequestDto;
import com.shoppingmall.dto.request.CartDetailRequestDto;
import com.shoppingmall.dto.request.CartUpdateRequestDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.CartService;
import com.shoppingmall.utils.SecurityUtils;
import com.shoppingmall.vo.CartTotalPrice;
import com.shoppingmall.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.shoppingmall.common.code.success.CommonSuccessCode.SUCCESS;
import static com.shoppingmall.common.code.success.cart.CartSuccessCode.UPDATE_CART;

// 좋은 API Response Body 만들기 : https://jojoldu.tistory.com/720

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class CartRestController {

    private final CartService cartService;

    /**
     * 장바구니 등록
     *
     * 비회원의 경우 UUID를 cart_uuid DB 필드에 저장하고 반환한다,
     * 후에 Client측에서 해당 UUID 함께 전달하여 비회원으로 판단하고 로그인 시에는 해당 UUID를 기반으로 장바구니 병합한다
     * 
     * 01) 쿠키를 사용하지 않은 이유는 A 서버에서 B 서버 요청 시 B서버에서 생성한 쿠키에 직접적인 접근이 불가능하기 때문
     * 02) 세션을 사용하지 않은 이유는 A 서버의 session과 B 서버의 session은 다르기 때문에 사용하지 않음
     */
    @PostMapping("/cart")
    public ResponseEntity<BaseResponse<?>> addCartItem(
            @AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
            @RequestBody @Valid CartUpdateRequestDto cartUpdateRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        Member member = null;
        if (principalUserDetails != null) {
            member = principalUserDetails.getMember();
        }

        if (SecurityUtils.isValidLoginMember(member)) {
            cartUpdateRequestDto.setMemberId(member.getMemberId());
            cartService.addOrUpdateCartProduct(cartUpdateRequestDto);
        } else if (!SecurityUtils.isValidLoginMember(member)){
            cartUpdateRequestDto.setUuid(StringUtils.isBlank(cartUpdateRequestDto.getUuid()) ? CommonUtils.generateRandomUUID() : cartUpdateRequestDto.getUuid());
            cartService.addOrUpdateCartProduct(cartUpdateRequestDto);
        } else {
            // 회원도 아니고 비회원도 아닌 경우 예외 처리
            log.error("장바구니 등록 중, 오류 발생!!");
            throw new RuntimeException("장바구니 등록 중, 알 수 없는 서버 오류가 발생했습니다. 다시 시도해주세요.");
        }
        return ApiResponseUtils.success(CartSuccessCode.SAVE_CART);
    }

    @GetMapping("/cart")
    public ResponseEntity<BaseResponse<?>> getCartItems(
            @AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
            @RequestParam(value = "uuid", required = false) String uuid) {

        Member member = null;
        if (principalUserDetails != null) {
            member = principalUserDetails.getMember();
        }

        CartTotalPrice cartItems = null;
        CartDetailRequestDto cartDetailRequestDto = new CartDetailRequestDto();
        if (SecurityUtils.isValidLoginMember(member)) {
            cartDetailRequestDto.setMemberId(member.getMemberId());
            cartItems = cartService.getCartItems(cartDetailRequestDto);
        } else if (!SecurityUtils.isValidLoginMember(member)) {
            cartDetailRequestDto.setUuid(uuid);
            cartItems = cartService.getCartItems(cartDetailRequestDto);
        }
        return ApiResponseUtils.success(SUCCESS, cartItems);
    }

    @PutMapping("/cart/update/{cartId}")
    public ResponseEntity<BaseResponse<?>> updateCartItem(
            @PathVariable("cartId") Integer cartId,
            @AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
            @RequestBody @Valid CartUpdateRequestDto cartUpdateRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        if (cartId == null) {
            return ApiResponseUtils.failure(CommonFailureCode.BAD_REQUEST);
        }

        Member member = null;
        if (principalUserDetails != null) {
            member = principalUserDetails.getMember();
        }

        if (SecurityUtils.isValidLoginMember(member)) {
            cartUpdateRequestDto.setMemberId(member.getMemberId());
            cartService.updateCartProducts(cartUpdateRequestDto);
        } else if (!SecurityUtils.isValidLoginMember(member)) {
            cartUpdateRequestDto.setUuid(StringUtils.isBlank(cartUpdateRequestDto.getUuid()) ? "" : cartUpdateRequestDto.getUuid());
            cartService.updateCartProducts(cartUpdateRequestDto);
        } else {
            log.error("장바구니 업데이트 중, 오류 발생!!");
            throw new RuntimeException("장바구니 업데이트 중, 알 수 없는 서버 오류가 발생했습니다. 다시 시도해주세요.");
        }
        return ApiResponseUtils.success(UPDATE_CART);
    }

    @DeleteMapping("/cart/delete/{cartId}")
    public ResponseEntity<BaseResponse<?>> deleteCartItem(
            @AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
            @PathVariable("cartId") Integer cartId) {

        if (cartId == null) {
            return ApiResponseUtils.failure(CommonFailureCode.BAD_REQUEST);
        }

        Member member = new Member();
        if (principalUserDetails != null) {
            member = principalUserDetails.getMember();
        }

        CartDeleteRequestDto cartDeleteRequestDto = new CartDeleteRequestDto();
        if (SecurityUtils.isValidLoginMember(member)) {
            cartDeleteRequestDto.setCartId(cartId);
            cartDeleteRequestDto.setMemberId(member.getMemberId());
            cartService.deleteCartItem(cartDeleteRequestDto);
        } else if (!SecurityUtils.isValidLoginMember(member)) {
            cartDeleteRequestDto.setCartId(cartId);
            cartDeleteRequestDto.setUuid(StringUtils.isBlank(cartDeleteRequestDto.getUuid()) ? "" : cartDeleteRequestDto.getUuid());
            cartService.deleteCartItem(cartDeleteRequestDto);
        } else {
            log.error("장바구니 삭제 중, 오류 발생!!");
            throw new RuntimeException("장바구니 삭제 중, 알 수 없는 서버 오류가 발생했습니다. 다시 시도해주세요.");
        }
        return ApiResponseUtils.success(CartSuccessCode.DELETE_CART);
    }
}
