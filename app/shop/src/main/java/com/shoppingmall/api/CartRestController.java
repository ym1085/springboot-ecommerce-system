//package com.shoppingmall.api;
//
//import com.shoppingmall.dto.request.CartItemRequestDto;
//import com.shoppingmall.dto.response.CartItemResponseDto;
//import com.shoppingmall.exception.InvalidParameterException;
//import com.shoppingmall.service.CartService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.security.Principal;
//import java.util.List;
//
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("/api/v1")
//@RestController
//public class CartRestController {
//
//    private final CartService cartService;
//
//    /**
//     * 장바구니 상품 추가
//     */
//    @PostMapping("/cart")
//    public ResponseEntity addCartItem(
//            @RequestBody @Valid CartItemRequestDto cartRequestDto,
//            BindingResult bindingResult,
//            Principal principal) {
//
//        if (bindingResult.hasErrors()) {
//            throw new InvalidParameterException(bindingResult);
//        }
//
//        Long cartItemId = cartService.addCartItem(cartRequestDto, principal.getName());
//        return ResponseEntity.ok(cartItemId);
//    }
//
//    /**
//     * 장바구니 목록 출력
//     */
//    @GetMapping("/cart")
//    public ResponseEntity getCartItems(Principal principal) {
//        List<CartItemResponseDto> cartDetails = cartService.getCartItems(principal.getName());
//        return ResponseEntity.ok(cartDetails);
//    }
//
//    @PutMapping("/{cartItemId}")
//    public ResponseEntity updateCartItem(
//            @PathVariable("cartItemId") Integer cartItemId,
//            @RequestBody CartItemRequestDto cartItemRequestDto,
//            BindingResult bindingResult,
//            Principal principal) {
//
//        if (bindingResult.hasErrors()) {
//            throw new InvalidParameterException(bindingResult);
//        }
//
//        if (cartService.validateCartItem(cartItemId, principal.getName())) {
//            throw new IllegalArgumentException();
//        }
//
//        int responseCode = cartService.updateCartItemCount(cartItemId, cartItemRequestDto.getCount());
//        return ResponseEntity.ok(responseCode);
//    }
//
//    @DeleteMapping("/{cartItemId}")
//    public ResponseEntity deleteCartItem(
//            @PathVariable("cartItemId") Integer cartItemId,
//            Principal principal) {
//
//        if (cartService.validateCartItem(cartItemId, principal.getName())) {
//            throw new IllegalArgumentException();
//        }
//
//        return ResponseEntity.ok(cartService.deleteCartItem(cartItemId));
//    }
//}
