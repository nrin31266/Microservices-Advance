package com.rin.inventoryservice.message.consumer;

import com.rin.inventoryservice.event.InventoryReservedEvent;
import com.rin.inventoryservice.event.consumer.OrderCreatedEvent;
import com.rin.inventoryservice.message.producer.InventoryEventProducer;
import com.rin.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class InventoryEventConsumer {

    InventoryService inventoryService;
    InventoryEventProducer inventoryEventProducer;

    @KafkaListener(
            topics = "orders",
            groupId = "inventory-service-order-created", // groupId chuẩn hóa
            containerFactory = "orderCreatedKafkaListenerContainerFactory" // Cần cấu hình trong config class
    )
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        System.out.println("📥 Nhận OrderCreatedEvent: " + event);
        // Xử lý sự kiện OrderCreatedEvent ở đây
        try{
            inventoryService.reserveInventory(event.getProductId(),  event.getQuantity());
            System.out.println("✅ Đã giữ hàng cho đơn hàng: " + event.getOrderId());
            inventoryEventProducer.publishInventoryReserved(
                    InventoryReservedEvent.builder()
                            .orderId(event.getOrderId())
                            .status("RESERVED")
                            .message("Hàng đã được giữ thành công.")
                            .build()
            );
        }catch (Exception e){
            System.out.println("❌ Giữ hàng thất bại cho đơn hàng: " + event.getOrderId() + ", Lỗi: " + e.getMessage());
            inventoryEventProducer.publishInventoryFailed(
                    InventoryReservedEvent.builder()
                            .orderId(event.getOrderId())
                            .status("FAILED")
                            .message("Giữ hàng thất bại vì: " + e.getMessage())
                            .build()
            );
        }
    }
}
