package com.rin.paymentservice.message.producer;

import com.rin.paymentservice.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_PAYMENT_COMPLETED = "payments";
    private static final String TOPIC_PAYMENT_FAILED = "payments_failed";

    public void sendPaymentCompleted(PaymentCompletedEvent event) {
        kafkaTemplate.send(TOPIC_PAYMENT_COMPLETED, String.valueOf(event.getOrderId()), event);
    }

//    public void sendPaymentFailed(PaymentFailedEvent event) {
//        kafkaTemplate.send(TOPIC_PAYMENT_FAILED, String.valueOf(event.getOrderId()), event);
//    }

}
