package com.ivanskyi.dto;

import com.ivanskyi.enumeration.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataAboutOrderStatusDTO {
    private Long orderId;
    private OrderStatus status;
}