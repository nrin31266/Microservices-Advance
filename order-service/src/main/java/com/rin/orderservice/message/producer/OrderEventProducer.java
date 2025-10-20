package com.rin.orderservice.message.producer;

import com.rin.orderservice.event.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
    public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_ORDER_CREATED = "orders";
    private static final String TOPIC_ORDER_COMPLETED = "orders_completed";

    public void publishOrderCreatedEvent(com.rin.orderservice.event.OrderCreatedEvent event) {
        System.out.println("📤 Gửi OrderCreatedEvent: " + event);
        kafkaTemplate.send(TOPIC_ORDER_CREATED, String.valueOf(event.getOrderId()), event);
    }

    public void publishOrderCompletedEvent(OrderCompletedEvent event) {
        System.out.println("📤 Gửi OrderCompletedEvent: " + event);
        kafkaTemplate.send(TOPIC_ORDER_COMPLETED, String.valueOf(event.getOrderId()), event);
    }


}
