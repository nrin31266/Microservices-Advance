package com.rin.notificationservice.message.consumer;

import com.rin.notificationservice.event.OrderCancelledEvent;
import com.rin.notificationservice.event.OrderCompletedEvent;
import com.rin.notificationservice.event.OrderPlacedEvent;
import com.rin.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "order-placed",
            groupId = "notification-service-order-placed",
            containerFactory = "orderPlacedEventListenerFactory")
    public void handleOrderEvent(OrderPlacedEvent event) {
        System.out.println("📨 Nhận được event từ Kafka: " + event);
        // Thực hiện gửi email ở đây
        emailService.sendOrderEmail(event);
    }

    @KafkaListener(
            topics = "orders_completed",
            groupId = "notification-service-order-completed",
            containerFactory = "orderCompletedEventListenerFactory"
    )
    public void handleOrderCompleted(OrderCompletedEvent event) {
        System.out.println("🔔 Gửi thông báo cho user "
                + event.getUserId()
                + " về đơn hàng "
                + event.getOrderId()
                + " trạng thái: "
                + event.getStatus());

    }
    @KafkaListener(
            topics = "orders_cancelled",
            groupId = "notification-service-order-cancelled",
            containerFactory = "orderCancelledKafkaListenerContainerFactory"
    )
    public void handleOrderCancelled(OrderCancelledEvent event) {
        System.out.println("🔔 Gửi thông báo cho user "
                + event.getUserId()
                + " về đơn hàng "
                + event.getOrderId()
                + " đã bị hủy.");
    }

}
