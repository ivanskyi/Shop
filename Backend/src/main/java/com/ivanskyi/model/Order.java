package com.ivanskyi.model;

import com.ivanskyi.enumeration.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @OneToMany(fetch = FetchType.EAGER)
    private List<PurchasedProduct> products;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Long createdAt;
    private Long closedAt;
    private Long lastProductAddAt;
}
