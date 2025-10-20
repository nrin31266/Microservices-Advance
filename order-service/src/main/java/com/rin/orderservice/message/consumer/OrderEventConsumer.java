package com.rin.orderservice.message.consumer;

import com.rin.orderservice.entity.OrderStatus;
import com.rin.orderservice.event.consumer.PaymentCompletedEvent;
import com.rin.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderService orderService;

//    @KafkaListener(
//            topics = "inventory_failed",
//            containerFactory = "inventoryFailedKafkaListenerContainerFactory"
//    )
//    public void handleInventoryFailed(InventoryFailedEvent event) {
//        System.out.println("📥 Nhận InventoryFailedEvent: " + event);
//        orderService.updateOrderStatus(event.getOrderId(), OrderStatus.INVENTORY_FAILED);
//    }

    @KafkaListener(
            topics = "payments",
            containerFactory = "paymentCompletedKafkaListenerContainerFactory"
    )
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        System.out.println("📥 Nhận PaymentCompletedEvent: " + event);
        orderService.updateOrderStatus(event.getOrderId(), OrderStatus.COMPLETED);
    }

//    @KafkaListener(
//            topics = "payments_failed",
//            containerFactory = "paymentFailedKafkaListenerContainerFactory"
//    )
//    public void handlePaymentFailed(PaymentFailedEvent event) {
//        System.out.println("📥 Nhận PaymentFailedEvent: " + event);
//        orderService.updateOrderStatus(event.getOrderId(), OrderStatus.PAYMENT_FAILED);
//    }
}
