package com.shoppingmall.mapper;

import com.shoppingmall.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductMapper {

    List<ProductVO> getProducts();

    Optional<ProductVO> getProductByProductId(Long productId);
}
