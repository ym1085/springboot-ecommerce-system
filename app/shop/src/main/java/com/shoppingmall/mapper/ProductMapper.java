package com.shoppingmall.mapper;

import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.vo.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductMapper {

    List<Product> getProducts(SearchRequestDto searchRequestDto);

    Optional<Product> getProductByProductId(Long productId);

    int count(SearchRequestDto searchRequestDto);

    int saveProducts(Product product);

    int updateProduct(Product product);

    int deleteProduct(Long productId);
}
