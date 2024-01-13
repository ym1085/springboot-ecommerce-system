//package com.shoppingmall.service;
//
//import com.shoppingmall.dto.request.CartItemRequestDto;
//import com.shoppingmall.dto.response.CartItemResponseDto;
//import com.shoppingmall.mapper.CartMapper;
//import com.shoppingmall.mapper.MemberMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Slf4j
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//@Service
//public class CartService {
//
//    private final MemberMapper memberMapper;
//    private final CartMapper cartMapper;
//
//    @Transactional
//    public Long addCartItem(CartItemRequestDto cartRequestDto, String memberAccount) {
//
//    }
//
//    public List<CartItemResponseDto> getCartItems(String memberAccount) {
//        memberMapper.getMemberByAccount(memberAccount)
//                .map(memberVO -> )
//    }
//
//    public boolean validateCartItem(int cartItemId, String name) {
//
//    }
//
//    public int updateCartItemCount(Integer cartItemId, Integer count) {
//
//    }
//
//    public int deleteCartItem(Integer cartItemId) {
//
//    }
//}