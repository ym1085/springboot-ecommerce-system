package com.shoppingmall.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.constant.ItemSellStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ProductVO {
    private Long productId;
    private int categoryId;
    private String categoryName;
    private String productName;
    private int productPrice;
    private int productStock;
    private String productDesc;
    private int productHits;
    private ItemSellStatus itemSellStatus;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    // 1(Product) : 1(ProductFile)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ProductFilesVO productFiles;

    public ProductVO(
            Long productId,
            int categoryId,
            String categoryName,
            String productName,
            int productPrice,
            int productStock,
            String productDesc,
            int productHits,
            ItemSellStatus itemSellStatus,
            LocalDateTime createDate,
            LocalDateTime updateDate,
            ProductFilesVO productFiles
    ) {
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
    }
}
