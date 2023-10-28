package com.ivanskyi.service.impl;

import com.ivanskyi.dto.OrderDTO;
import com.ivanskyi.dto.DataAboutOrderStatusDTO;
import com.ivanskyi.dto.PurchasedProductDTO;
import com.ivanskyi.enumeration.OrderStatus;
import com.ivanskyi.model.Order;
import com.ivanskyi.model.PurchasedProduct;
import com.ivanskyi.repository.OrderRepository;
import com.ivanskyi.repository.OrderedProductRepository;
import com.ivanskyi.repository.ProductRepository;
import com.ivanskyi.service.OrderService;
import com.ivanskyi.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserSecurityServiceImpl userSecurityServiceImpl;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderedProductRepository orderedProductRepository;

    public final PurchasedProductDTO addNewProductToOrder(final PurchasedProductDTO orderProductDto) {
        log.info("Started process to add product with id: {} to order with id: {}",
                orderProductDto.getProduct(), orderProductDto.getOrderId());
        final Order order = fetchOrderToAddProduct();
        final PurchasedProduct product = fetchPurchasedProduct(order, orderProductDto);
        log.info("PurchasedProduct is part of order with id:{}", product.getOrderId());
        addProductToOrder(order, product);
        log.info("Finished process to add product with id: {} to order with id: {}",
                orderProductDto.getProduct().getId(), orderProductDto.getOrderId());
        orderProductDto.setOrderId(order.getId());
        return orderProductDto;
    }

    private Order fetchOrderToAddProduct() {
        List<Order> notClosedOrders = getLastNotClosedOrder();
        return notClosedOrders.isEmpty() ? createNewOrder() : notClosedOrders.get(0);
    }

    private List<Order> getLastNotClosedOrder() {
        return orderRepository.findByUserIdAndStatus(getCurrentUser(), OrderStatus.OPENED);
    }

    private Order createNewOrder() {
        Order order = new Order();
        order.setStatus(OrderStatus.OPENED);
        order.setUserId(getCurrentUser());
        order.setCreatedAt(DateTimeUtil.getCurrentEpochTime());
        return orderRepository.save(order);
    }

    private Long getCurrentUser() {
        return userSecurityServiceImpl.getUserByUsername().getId();
    }

    private PurchasedProduct fetchPurchasedProduct(final Order order, final PurchasedProductDTO orderProductDto) {
        List<PurchasedProduct> matchingProducts = findMatchingPurchasedProducts(order, orderProductDto.getProduct().getId());
        return matchingProducts.isEmpty() ? createNewPurchasedProduct(order, orderProductDto) :
                updateExistingPurchasedProduct(matchingProducts.get(0), orderProductDto);
    }

    private List<PurchasedProduct> findMatchingPurchasedProducts(final Order order, final Long productId) {
        return orderedProductRepository.findAllByOrderId(order.getId())
                .stream()
                .filter(purchasedProduct -> purchasedProduct.getProduct().getId().equals(productId))
                .collect(Collectors.toList());
    }

    private PurchasedProduct updateExistingPurchasedProduct(final PurchasedProduct purchasedProduct, final PurchasedProductDTO orderProductDto) {
        purchasedProduct.setCount(purchasedProduct.getCount() + orderProductDto.getAmount());
        return orderedProductRepository.save(purchasedProduct);
    }

    private PurchasedProduct createNewPurchasedProduct(final Order order, final PurchasedProductDTO orderProductDto) {
        PurchasedProduct purchasedProduct = new PurchasedProduct();
        purchasedProduct.setProduct(productRepository.findById(orderProductDto.getProduct().getId()).orElseThrow());
        purchasedProduct.setCount(orderProductDto.getAmount());
        purchasedProduct.setOrderId(order.getId());
        return orderedProductRepository.save(purchasedProduct);
    }

    private void addProductToOrder(final Order order, final PurchasedProduct product) {
        if (Objects.isNull(order.getProducts())) {
            order.setProducts(List.of(product));
        } else {
            order.getProducts().add(product);
        }
        order.setLastProductAddAt(DateTimeUtil.getCurrentEpochTime());
        orderRepository.save(order);
    }

    public final List<OrderDTO> getAllOrdersForCurrentUser() {
        return OrderDTO.fromOrders(orderRepository.findAllByUserId(getCurrentUser()));
    }

    public final Order saveOrder(final Order order) {
        return orderRepository.save(order);
    }

    public final List<Order> getUnpaidOrders() {
        return orderRepository.findByStatus(OrderStatus.OPENED);
    }

    public final boolean executePaymentForOrder(final DataAboutOrderStatusDTO dataAboutOrderStatusDTO) {
        log.info("Payment process initiated for order (ID: {})", dataAboutOrderStatusDTO.getOrderId());
        Optional<Order> orderToProcess = orderRepository.findById(dataAboutOrderStatusDTO.getOrderId());
        if (orderToProcess.isPresent()) {
            Order order = orderToProcess.get();
            order.setStatus(OrderStatus.ORDERED);
            order.setClosedAt(DateTimeUtil.getCurrentEpochTime());
            orderRepository.save(order);
            log.info("Payment for order (ID: {}) completed successfully", dataAboutOrderStatusDTO.getOrderId());
            return true;
        } else {
            log.warn("Order with ID {} was not found", dataAboutOrderStatusDTO.getOrderId());
            return false;
        }
    }
}
