package com.shoppingmall.vo;

import com.shoppingmall.constant.ItemSellStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Product {
    private Integer memberId;
    private Integer productId;
    private Integer categoryId;
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
    private ProductFiles productFiles;
}
