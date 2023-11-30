package com.shoppingmall.repository;

import com.shoppingmall.dto.request.ProductRequestDto;
import com.shoppingmall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new com.shoppingmall.dto.request.ProductRequestDto(p.productName, p.productDesc, p.productPrice) " +
            "FROM Product p")
    List<ProductRequestDto> findAllProductsWithFiles();
}