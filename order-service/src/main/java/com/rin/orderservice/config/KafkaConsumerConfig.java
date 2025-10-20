package com.rin.orderservice.config;

import com.rin.orderservice.event.consumer.PaymentCompletedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Map<String, Object> baseProps(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }


    // PaymentCompletedEvent
    // Cấu hình Consumer cơ bản và Deserializer.
    // Để biết cách tạo ra Consumer và giải mã (deserialize) message thành đối tượng Java của sự kiện đó
    @Bean
    public ConsumerFactory<String, PaymentCompletedEvent> paymentCompletedConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                baseProps("order-service-payment-completed"),
                new StringDeserializer(),
                new JsonDeserializer<>(PaymentCompletedEvent.class, false)
        );
    }

    // Cấu hình Môi trường lắng nghe/Thực thi
    // Để quản lý việc lắng nghe và thực thi các hàm @KafkaListener sử dụng cấu hình từ ConsumerFactory đó.
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> paymentCompletedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentCompletedConsumerFactory());
        return factory;
    }

    // InventoryFailedEvent
//    @Bean
//    public ConsumerFactory<String, InventoryFailedEvent> inventoryFailedConsumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(
//                baseProps("order-service-inventory-failed"),
//                new StringDeserializer(),
//                new JsonDeserializer<>(InventoryFailedEvent.class, false)
//        );
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, InventoryFailedEvent> inventoryFailedKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, InventoryFailedEvent> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(inventoryFailedConsumerFactory());
//        return factory;
//    }

//    // PaymentFailedEvent
//    @Bean
//    public ConsumerFactory<String, PaymentFailedEvent> paymentFailedConsumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(
//                baseProps("order-service-payment-failed"),
//                new StringDeserializer(),
//                new JsonDeserializer<>(PaymentFailedEvent.class, false)
//        );
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, PaymentFailedEvent> paymentFailedKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, PaymentFailedEvent> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(paymentFailedConsumerFactory());
//        return factory;
//    }
}
