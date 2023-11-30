package com.shoppingmall.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_desc")
    private String productDesc;

    @Column(name = "product_price")
    private Integer productPrice;

    @OneToMany(mappedBy = "product")
    private List<ProductFile> productFiles;

}
