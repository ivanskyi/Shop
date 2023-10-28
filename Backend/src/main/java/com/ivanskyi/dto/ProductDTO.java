package com.ivanskyi.dto;

import com.ivanskyi.model.Product;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private Double price;
    private Integer quantity;

    public static ProductDTO fromEntity(Product product) {
        ProductDTO productDto = new ProductDTO();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setImageUrl(product.getImageUrl());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        return productDto;
    }

    public Product toEntity() {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setImageUrl(imageUrl);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        return product;
    }
}
