package com.ivanskyi.dto;

import com.ivanskyi.enumeration.OrderStatus;
import com.ivanskyi.model.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private List<PurchasedProductDTO> products;
    private OrderStatus status;
    private Long createdAt;
    private Long closedAt;
    private Long lastProductAddAt;

    public static OrderDTO fromOrder(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setProducts(order.getProducts().stream()
                .map(PurchasedProductDTO::new)
                .collect(Collectors.toList()));
        orderDTO.setStatus(order.getStatus());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setClosedAt(order.getClosedAt());
        orderDTO.setLastProductAddAt(order.getLastProductAddAt());
        return orderDTO;
    }

    public static List<OrderDTO> fromOrders(List<Order> orders) {
        return orders.stream()
                .map(OrderDTO::fromOrder)
                .collect(Collectors.toList());
    }
}