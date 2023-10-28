package com.ivanskyi.job;

import com.ivanskyi.enumeration.OrderStatus;
import com.ivanskyi.model.Order;
import com.ivanskyi.service.OrderService;
import com.ivanskyi.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoveNonPaidOrder {

    private final OrderService orderService;

    @Scheduled(fixedDelay = 60000)
    public void startJobToRemoveExtraOrders() {
        log.info("Started a job to remove orders that weren't confirmed in the last 10 minutes.");
        final List<Order> allNonPaidOrders = orderService.getUnpaidOrders();
        allNonPaidOrders.stream()
                .filter(this::isOrderOlderThanTenMinutes)
                .forEach(this::markOrderAsCanceled);
        log.info("Finished a job to remove orders that weren't confirmed in the last 10 minutes.");
    }

    private boolean isOrderOlderThanTenMinutes(final Order order) {
        long createdAt = order.getCreatedAt();
        long currentEpochTime = DateTimeUtil.getCurrentEpochTime();
        long timeDifferenceInMinutes = DateTimeUtil.calculateTimeDifferenceInMinutes(createdAt, currentEpochTime);
        log.info("timeDifferenceInMinutes: {}", timeDifferenceInMinutes);
        return timeDifferenceInMinutes >= 10;
    }

    private void markOrderAsCanceled(final Order order) {
        order.setStatus(OrderStatus.CANCELLED);
        order.setClosedAt(DateTimeUtil.getCurrentEpochTime());
        orderService.saveOrder(order);
    }
}
