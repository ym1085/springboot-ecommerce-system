package com.shoppingmall.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product_file")
@Getter
@Setter
public class ProductFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productFileId;

    @ManyToOne
    @JoinColumn(name = "product_number", referencedColumnName = "product_id")
    private Product product;

    @Column(name = "stored_file_name")
    private String storedFileName;

}
