package com.rin.inventoryservice.message.consumer;

import com.rin.inventoryservice.entity.ReservedOrder;
import com.rin.inventoryservice.event.InventoryFailedEvent;
import com.rin.inventoryservice.event.InventoryReservedEvent;
import com.rin.inventoryservice.event.consumer.OrderCancelledEvent;
import com.rin.inventoryservice.event.consumer.OrderCreatedEvent;
import com.rin.inventoryservice.message.producer.InventoryEventProducer;
import com.rin.inventoryservice.repository.InventoryRepository;
import com.rin.inventoryservice.repository.ReservedOrderRepository;
import com.rin.inventoryservice.service.InventoryService;
import jakarta.transaction.Transactional;
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
    InventoryRepository inventoryRepository;
    ReservedOrderRepository reservedOrderRepository;

    @Transactional
    @KafkaListener(
            topics = "orders",
            groupId = "inventory-service-order-created", // groupId chuẩn hóa
            containerFactory = "orderCreatedKafkaListenerContainerFactory" // Cần cấu hình trong config class
    )
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        System.out.println("📥 Nhận OrderCreatedEvent: " + event);
        // Xử lý sự kiện OrderCreatedEvent ở đây
        try{
            int res = inventoryRepository.decreaseStockIfAvailable(event.getProductId(), event.getQuantity());
            if (res == 0) {
                throw new RuntimeException("Số lượng hàng không đủ để giữ: " + event.getQuantity());
            }
            // Lưu thông tin đơn hàng đã giữ
            reservedOrderRepository.insertIfNotExists(event.getOrderId(), event.getProductId(), event.getQuantity());


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
                    InventoryFailedEvent.builder()
                            .orderId(event.getOrderId())
                            .status("FAILED")
                            .message("Giữ hàng thất bại: " + e.getMessage())
                            .build()
            );
        }
    }
    @Transactional
    @KafkaListener(
            topics = "orders_cancelled",
            groupId = "inventory-service-order-cancelled", // groupId chuẩn hóa
            containerFactory = "orderCancelledKafkaListenerContainerFactory" // Cần cấu hình trong config class
    )
    public void handleOrderCancelledEvent(OrderCancelledEvent event) {
        System.out.println("📥 Nhận OrderCancelledEvent: " + event);
        // Xử lý sự kiện OrderCancelledEvent ở đây
        ReservedOrder reservedOrder = reservedOrderRepository.findByOrderIdAndProductId(event.getOrderId(), event.getProductId()).orElse(null);
        if(reservedOrder != null){
            // Hoàn trả hàng
            inventoryRepository.increaseStock(event.getProductId(), reservedOrder.getQuantity());
            // Xóa thông tin đơn hàng đã giữ
            reservedOrderRepository.deleteByOrderIdAndProductId(event.getOrderId(), event.getProductId());

            System.out.println("✅ Đã hoàn trả hàng cho đơn hàng: " + event.getOrderId() +
                    ", productId: " + event.getProductId()+
                    ", quantity: " + event.getQuantity()+
                    ", reason: " + event.getReason());
        }else{
            // Bo qua
            System.out.println("⚠️ Không tìm thấy đơn hàng đã giữ, bỏ qua hoàn trả hàng cho đơn hàng: " + event.getOrderId());
        }


    }
}
