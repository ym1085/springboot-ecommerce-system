package com.shoppingmall.dto.request;

import com.shoppingmall.constant.DirPathType;
import com.shoppingmall.constant.ItemSellStatus;
import com.shoppingmall.vo.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveRequestDto {
    private Long productId;
    private Long categoryId;
    private String productName;
    private Integer productPrice;
    private Integer productStock;
    private String productDesc;
    private ItemSellStatus itemSellStatus = ItemSellStatus.SELL;

    private List<MultipartFile> files = new ArrayList<>();
    private DirPathType dirPathType = DirPathType.products;

    public Product toEntity() {
        return Product.builder()
                .categoryId(categoryId)
                .productName(productName)
                .productPrice(productPrice)
                .productStock(productStock)
                .productDesc(productDesc)
                .itemSellStatus(itemSellStatus)
                .build();
    }
}
