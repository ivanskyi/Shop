package com.ivanskyi.service.impl;

import com.ivanskyi.dto.ProductDTO;
import com.ivanskyi.model.Product;
import com.ivanskyi.repository.ProductRepository;
import com.ivanskyi.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public Optional<Product> getProductById(Long productId) {
       return productRepository.findById(productId);
    }


    public List<ProductDTO> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ProductDTO createNewProduct(final ProductDTO productDto) {
        log.info("Got request to create a product with name: {}", productDto.getName());
        Product entity = productDto.toEntity();
        return ProductDTO.fromEntity(productRepository.save(entity));
    }
}
