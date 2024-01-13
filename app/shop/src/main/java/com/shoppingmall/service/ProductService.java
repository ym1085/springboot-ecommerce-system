package com.shoppingmall.service;

import com.shoppingmall.dto.response.ProductDetailResponseDto;
import com.shoppingmall.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductMapper productMapper;

    public List<ProductDetailResponseDto> getProducts() {
        return productMapper.getProducts().stream()
                .map(ProductDetailResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public ProductDetailResponseDto getProductByProductId(Long productId) {
        return productMapper.getProductByProductId(productId)
                .map(ProductDetailResponseDto::toDto)
                .orElse(new ProductDetailResponseDto());
    }
}