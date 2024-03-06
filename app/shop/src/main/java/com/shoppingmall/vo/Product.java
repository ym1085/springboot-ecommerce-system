package com.shoppingmall.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.constant.ItemSellStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Product {
    private Long memberId;
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

    // 1(Product) : 1(ProductFile)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ProductFiles productFiles;

    @Builder
    public Product(
            Long memberId,
            Long productId,
            Long categoryId,
            String categoryName,
            String productName,
            int productPrice,
            int productStock,
            String productDesc,
            int productHits,
            ItemSellStatus itemSellStatus,
            LocalDateTime createDate,
            LocalDateTime updateDate,
            ProductFiles productFiles,
            String delYn
    ) {
        this.memberId = memberId;
        this.productId = productId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productDesc = productDesc;
        this.productHits = productHits;
        this.itemSellStatus = itemSellStatus;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.productFiles = productFiles;
        this.delYn = delYn;
    }
}
