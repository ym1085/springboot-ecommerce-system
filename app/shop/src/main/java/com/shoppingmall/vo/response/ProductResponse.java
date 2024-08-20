package com.shoppingmall.vo.response;

import com.shoppingmall.vo.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProductResponse {
    private List<Product> productByPhones;
    private List<Product> productByWatches;

    @Builder
    public ProductResponse(List<Product> productByPhones, List<Product> productByWatches) {
        this.productByPhones = productByPhones;
        this.productByWatches = productByWatches;
    }
}