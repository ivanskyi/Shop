package com.ivanskyi.dto;

import com.ivanskyi.model.PurchasedProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedProductDTO {
    private Long id;
    private ProductDTO product;
    private Long orderId;
    private Long amount;

    public PurchasedProductDTO(PurchasedProduct entity) {
        this.id = entity.getId();
        this.product = ProductDTO.fromEntity(entity.getProduct());
        this.orderId = entity.getOrderId();
        this.amount = entity.getCount();
    }

    public static PurchasedProductDTO fromEntity(PurchasedProduct entity) {
        return new PurchasedProductDTO(
                entity.getId(),
                ProductDTO.fromEntity(entity.getProduct()),
                entity.getOrderId(),
                entity.getCount()
        );
    }

    public PurchasedProduct toEntity() {
        PurchasedProduct entity = new PurchasedProduct();
        entity.setId(id);
        entity.setProduct(product.toEntity());
        entity.setOrderId(orderId);
        entity.setCount(amount);
        return entity;
    }
}
