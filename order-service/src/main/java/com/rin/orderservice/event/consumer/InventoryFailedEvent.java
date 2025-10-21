package com.rin.orderservice.event.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryFailedEvent {
    private Long orderId;
    private String message;
    private String status;
}
