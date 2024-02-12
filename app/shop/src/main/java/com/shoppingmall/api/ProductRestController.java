package com.shoppingmall.api;

import com.shoppingmall.common.response.ApiUtils;
import com.shoppingmall.common.response.CommonResponse;
import com.shoppingmall.common.response.SuccessCode;
import com.shoppingmall.dto.request.ProductSaveRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.PagingResponseDto;
import com.shoppingmall.dto.response.ProductDetailResponseDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ProductRestController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<CommonResponse> getProducts(SearchRequestDto searchRequestDto) {
        PagingResponseDto<ProductDetailResponseDto> products = productService.getProducts(searchRequestDto);
        return ApiUtils.success(
                SuccessCode.OK.getHttpStatus(),
                SuccessCode.OK.getMessage(),
                products
        );
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<CommonResponse> getProductByProductId(@PathVariable("productId") Long productId) {
        ProductDetailResponseDto productItemResponseDto = productService.getProductByProductId(productId);
        return ApiUtils.success(
                SuccessCode.OK.getHttpStatus(),
                SuccessCode.OK.getMessage(),
                productItemResponseDto
        );
    }

    @PostMapping("/products")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse> saveProducts(
            @Valid @ModelAttribute ProductSaveRequestDto productRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        productService.saveProducts(productRequestDto);

        return ApiUtils.success(
                SuccessCode.OK.getHttpStatus(),
                SuccessCode.OK.getMessage()
        );
    }
}
