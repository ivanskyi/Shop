package com.ivanskyi.service;

import com.ivanskyi.dto.OrderDTO;
import com.ivanskyi.dto.DataAboutOrderStatusDTO;
import com.ivanskyi.dto.PurchasedProductDTO;
import com.ivanskyi.model.Order;

import java.util.List;

public interface OrderService {
    PurchasedProductDTO addNewProductToOrder(PurchasedProductDTO orderProductDto);

    List<OrderDTO> getAllOrdersForCurrentUser();

    Order saveOrder(Order order);

    List<Order> getUnpaidOrders();

    boolean executePaymentForOrder(DataAboutOrderStatusDTO dataAboutOrderStatusDTO);
}
