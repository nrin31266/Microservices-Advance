package com.rin.inventoryservice.event;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@Builder
public class InventoryReservedEvent {
    Long orderId;
    String status;
    String message;

}
