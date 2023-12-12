package com.shoppingmall.api;

import com.shoppingmall.common.CommonResponse;
import com.shoppingmall.common.MessageCode;
import com.shoppingmall.common.ResponseFactory;
import com.shoppingmall.dto.request.ProductRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.PagingResponseDto;
import com.shoppingmall.dto.response.PostResponseDto;
import com.shoppingmall.dto.response.ProductResponseDto;
import com.shoppingmall.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class ProductRestController {
    @Autowired
    ProductService productService;


    @GetMapping(value = "/post")
    public ResponseEntity<ProductResponseDto> getProductsAll(ProductRequestDto productRequestDto) {
        PagingResponseDto<ProductRequestDto> posts = productService.getProductsAll(productRequestDto);
        return ResponseFactory.createResponseFactory(MessageCode.SUCCESS_GET_POSTS.getCode(), MessageCode.SUCCESS_GET_POSTS.getMessage(), posts, HttpStatus.OK);
    }
}
