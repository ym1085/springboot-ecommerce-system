package com.shoppingmall.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.constant.ItemSellStatus;
import com.shoppingmall.vo.ProductFiles;
import com.shoppingmall.vo.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailResponseDto {
    private Long productId;
    private Long categoryId;
    private String categoryName;
    private String productName;
    private int productPrice;
    private int productStock;
    private String productDesc;
    private int productHits;
    private ItemSellStatus itemSellStatus;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String delYn;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProductFiles productFiles;

    public static ProductDetailResponseDto toDto(Product product) {
        return ProductDetailResponseDto.builder()
                .productId(product.getProductId())
                .categoryId(product.getCategoryId())
                .categoryName(product.getCategoryName())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productStock(product.getProductStock())
                .productDesc(product.getProductDesc())
                .productHits(product.getProductHits())
                .itemSellStatus(product.getItemSellStatus())
                .createDate(product.getCreateDate())
                .updateDate(product.getUpdateDate())
                .delYn(product.getDelYn())
                .productFiles(product.getProductFiles())
                .build();
    }
}