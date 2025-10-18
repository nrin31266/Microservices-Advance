package com.rin.orderservice.event.consumer;

import lombok.Data;

@Data
public class PaymentCompletedEvent {
    private Long orderId;
    private String paymentId;
    private double amount;
}
