package com.shoppingmall.mapper;

import com.shoppingmall.dto.request.ProductSaveRequestDto;
import com.shoppingmall.dto.request.ProductUpdateRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.vo.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductMapper {

    List<Product> getProducts(SearchRequestDto searchRequestDto);

    Optional<Product> getProductByProductId(Integer productId);

    int count(SearchRequestDto searchRequestDto);

    int countByProductName(ProductSaveRequestDto product);

    int saveProducts(ProductSaveRequestDto product);

    int updateProduct(ProductUpdateRequestDto product);

    int deleteProduct(Integer productId);

    String getCategoryNameByProductCategoryId(Integer categoryId);
}
