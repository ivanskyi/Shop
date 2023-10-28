package com.ivanskyi.controller;

import com.ivanskyi.dto.ProductDTO;
import com.ivanskyi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_CLIENT')")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<ProductDTO> createNewProduct(@RequestBody final ProductDTO productDTO) {
        final ProductDTO createdProduct = productService.createNewProduct(productDTO);
        return createdProduct != null
                ? ResponseEntity.status(HttpStatus.CREATED).body(createdProduct)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
