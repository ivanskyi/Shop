package com.ivanskyi.service;

import com.ivanskyi.dto.ProductDTO;
import com.ivanskyi.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> getProductById(Long productId);

    List<ProductDTO> getAllProducts();

    ProductDTO createNewProduct(ProductDTO productDto);
}
