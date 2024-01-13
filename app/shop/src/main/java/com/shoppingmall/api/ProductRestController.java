package com.shoppingmall.api;

import com.shoppingmall.common.response.ApiUtils;
import com.shoppingmall.common.response.CommonResponse;
import com.shoppingmall.common.success.CommonSuccessCode;
import com.shoppingmall.dto.response.ProductDetailResponseDto;
import com.shoppingmall.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ProductRestController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<CommonResponse> getProducts() {
        List<ProductDetailResponseDto> products = productService.getProducts();
        return ApiUtils.success(
                CommonSuccessCode.SUCCESS_CODE.getHttpStatus(),
                CommonSuccessCode.SUCCESS_CODE.getMessage(),
                products
        );
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<CommonResponse> getProductByProductId(@PathVariable("productId") Long productId) {
        ProductDetailResponseDto productItemResponseDto = productService.getProductByProductId(productId);
        return ApiUtils.success(
                CommonSuccessCode.SUCCESS_CODE.getHttpStatus(),
                CommonSuccessCode.SUCCESS_CODE.getMessage(),
                productItemResponseDto
        );
    }
}
