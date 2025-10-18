package com.rin.orderservice.event;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderCreatedEvent {
    private Long orderId;
    private Long userId;
    private String productId;
    private int quantity;
    private double total;
}
