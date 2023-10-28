package com.ivanskyi.repository;

import com.ivanskyi.model.PurchasedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderedProductRepository extends JpaRepository<PurchasedProduct, Long> {
    List<PurchasedProduct> findAllByOrderId(Long orderId);
}
