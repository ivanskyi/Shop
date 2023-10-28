package com.ivanskyi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "purchased_product")
public class PurchasedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    Product product;

    Long orderId;

    Long count;
}
