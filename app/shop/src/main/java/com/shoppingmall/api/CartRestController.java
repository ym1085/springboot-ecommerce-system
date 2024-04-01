package com.shoppingmall.api;

import com.shoppingmall.common.response.ApiUtils;
import com.shoppingmall.common.response.CommonResponse;
import com.shoppingmall.common.response.ErrorCode;
import com.shoppingmall.common.response.SuccessCode;
import com.shoppingmall.common.utils.CommonUtils;
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
import org.apache.commons.lang3.StringUtils;
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

    /**
     * 장바구니 등록
     *
     * @param cartUUID
     * 비회원의 경우 UUID를 cart_uuid DB 필드에 저장하고 반환한다,
     * 후에 Client측에서 해당 UUID 함께 전달하여 비회원으로 판단하고 로그인 시에는 해당 UUID를 기반으로 장바구니 병합한다
     * 
     * 01) 쿠키를 사용하지 않은 이유는 A 서버에서 B 서버 요청 시 B서버에서 생성한 쿠키에 직접적인 접근이 불가능하기 때문
     * 02) 세션을 사용하지 않은 이유는 A 서버의 session과 B 서버의 session은 다르기 때문에 사용하지 않음
     */
    @PostMapping("/cart")
    public ResponseEntity<CommonResponse> addCartItem(
            @RequestParam(value = "cartUUID", required = false) String cartUUID,
            @AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
            @RequestBody @Valid CartSaveRequestDto cartSaveRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        Member member = new Member();
        if (principalUserDetails != null) {
            member = principalUserDetails.getMember();
        }

        if (SecurityUtils.isValidLoginMember(member)) { // 회원 장바구니 등록
            cartSaveRequestDto.setMemberId(member.getMemberId());
            cartService.addCartProductForMember(cartSaveRequestDto);
            return ApiUtils.success(SuccessCode.SAVE_CART.getCode(), SuccessCode.SAVE_CART.getHttpStatus(), SuccessCode.SAVE_CART.getMessage());
        } else if (!SecurityUtils.isValidLoginMember(member)){ // 비회원 장바구니 등록
            // TODO: 신규 ResponseDto 하나 생성 후 -> key : value 형태로 Client에 반환
            String generatedCartUUID = StringUtils.isNotBlank(cartUUID) ? cartUUID : CommonUtils.generateRandomUUID();
            cartSaveRequestDto.setUuid(generatedCartUUID);
            cartService.addCartProductForMember(cartSaveRequestDto);
            return ApiUtils.success(SuccessCode.SAVE_CART.getCode(), SuccessCode.SAVE_CART.getHttpStatus(), SuccessCode.SAVE_CART.getMessage(), generatedCartUUID);
        } else { // 회원도 아니고 비회원도 아닌 경우 예외 처리
            throw new RuntimeException("장바구니 등록 중, 알 수 없는 서버 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    @GetMapping("/cart")
    public ResponseEntity<CommonResponse> getCartItems(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails) {

        Member member = new Member();
        if (principalUserDetails != null) {
            member = principalUserDetails.getMember();
        }

        // TODO: 회원, 비회원 구분하여 장바구니 목록 출력
        if (SecurityUtils.isValidLoginMember(member)) {
            //TODO
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

        Member member = new Member();
        if (principalUserDetails != null) {
            member = principalUserDetails.getMember();
        }

        // TODO: 회원, 비회원 구분하여 장바구니 목록 수정
        if (SecurityUtils.isValidLoginMember(member)) {
            //TODO
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

        Member member = new Member();
        if (principalUserDetails != null) {
            member = principalUserDetails.getMember();
        }

        if (SecurityUtils.isValidLoginMember(member)) {
            //TODO
        }

        cartService.deleteCartItem(cartId, member.getMemberId());

        return ApiUtils.success(SuccessCode.DELETE_CART.getCode(), SuccessCode.DELETE_CART.getHttpStatus(), SuccessCode.DELETE_CART.getMessage());
    }
}
