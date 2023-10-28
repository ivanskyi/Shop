package com.ivanskyi.repository;


import com.ivanskyi.enumeration.OrderStatus;
import com.ivanskyi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdAndStatus(Long userId, OrderStatus orderStatus);

    List<Order> findByStatus(OrderStatus orderStatus);

    List<Order> findAllByUserId(Long userId);
}
