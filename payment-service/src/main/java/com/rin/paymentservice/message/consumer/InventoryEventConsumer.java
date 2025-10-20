package com.rin.paymentservice.message.consumer;

import com.rin.paymentservice.event.consumer.InventoryReservedEvent;
import com.rin.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {
    private final PaymentService paymentService;

    @KafkaListener(topics = "inventory-reserved", groupId = "payment-service")
    public void handleInventoryReserved(InventoryReservedEvent event) {
        System.out.printf("📥 PaymentService nhận được InventoryReservedEvent: orderId=%d, status=%s, message=%s%n",
                event.getOrderId(), event.getStatus(), event.getMessage());

        paymentService.processPayment(event);
    }

}
