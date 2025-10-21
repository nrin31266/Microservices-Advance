package com.rin.paymentservice.service;

import com.rin.paymentservice.event.PaymentCompletedEvent;
import com.rin.paymentservice.event.PaymentFailedEvent;
import com.rin.paymentservice.event.consumer.InventoryReservedEvent;
import com.rin.paymentservice.message.producer.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final KafkaProducerService kafkaProducerService;

    public void processPayment(InventoryReservedEvent event) {
        try {
            int randomNum = (int) (Math.random() * 10);
            boolean success = randomNum < 8; // 80% cơ hội thành công
//            boolean success = false;
            if (success) {
                PaymentCompletedEvent completed = new PaymentCompletedEvent();
                completed.setOrderId(event.getOrderId());
                completed.setPaymentId(UUID.randomUUID().toString());
                completed.setAmount(100.0); // ví dụ cố định

                kafkaProducerService.sendPaymentCompleted(completed);
                System.out.println("💳 Payment success -> gửi PaymentCompletedEvent");
            }else{
                System.out.println("💳 Payment failed -> gửi PaymentFailedEvent");
                kafkaProducerService.sendPaymentFailed(PaymentFailedEvent.builder()
                                .orderId(event.getOrderId())
                                .reason("Thanh toán không đủ hạn mức.")
                        .build());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
