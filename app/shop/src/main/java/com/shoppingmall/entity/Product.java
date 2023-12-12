package com.shoppingmall.entity;

import javax.persistence.*;

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

    @OneToOne(mappedBy = "product")
    private ProductFile productFiles;

}
