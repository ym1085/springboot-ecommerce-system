package com.shoppingmall.service;

import com.shoppingmall.dto.request.ProductRequestDto;
import com.shoppingmall.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Transactional(readOnly = true)
    public List<ProductRequestDto> getProductsAll() {
        return productRepository.findAllProductsWithFiles();
    }


}
