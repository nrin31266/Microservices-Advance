package com.rin.inventoryservice.config;

import com.rin.inventoryservice.event.consumer.OrderCancelledEvent;
import com.rin.inventoryservice.event.consumer.OrderCreatedEvent;
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
    // Cấu hình Consumer cơ bản và Deserializer.
    // Để biết cách tạo ra Consumer và giải mã (deserialize) message thành đối tượng Java của sự kiện đó
    @Bean
    public ConsumerFactory<String, OrderCreatedEvent> orderCreatedConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                baseProps("inventory-service-order-created"),
                new StringDeserializer(),
                new JsonDeserializer<>(OrderCreatedEvent.class, false)
        );
    }

    // Cấu hình Môi trường lắng nghe/Thực thi
    // Để quản lý việc lắng nghe và thực thi các hàm @KafkaListener sử dụng cấu hình từ ConsumerFactory đó.
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> orderCreatedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderCreatedConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, OrderCancelledEvent> orderCancelledConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                baseProps("inventory-service-order-cancelled"),
                new StringDeserializer(),
                new JsonDeserializer<>(OrderCancelledEvent.class, false)
        );
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCancelledEvent> orderCancelledKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCancelledEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderCancelledConsumerFactory());
        return factory;
    }


}
