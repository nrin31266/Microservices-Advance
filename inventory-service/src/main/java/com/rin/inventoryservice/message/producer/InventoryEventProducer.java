package com.rin.inventoryservice.message.producer;

import com.rin.inventoryservice.event.InventoryReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventProducer {
    // Spring tự động inject KafkaTemplate với các cấu hình Producer đã có
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_RESERVED = "inventory-reserved";
    private static final String TOPIC_FAILED = "inventory-failed";


    public void publishInventoryReserved(InventoryReservedEvent event) {
        // Gửi message, sử dụng orderID làm key để đảm bảo thứ tự xử lý
        kafkaTemplate.send(TOPIC_RESERVED, event.getOrderId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("✅ Gửi InventoryReserved thành công cho Order {} tới topic {}", event.getOrderId(), TOPIC_RESERVED);
                    } else {
                        log.error("❌ Lỗi gửi InventoryReserved cho Order {}: {}", event.getOrderId(), ex.getMessage());
                    }
                });
    }


    public void publishInventoryFailed(InventoryReservedEvent event) {

        // Gửi message, sử dụng orderID làm key
        kafkaTemplate.send(TOPIC_FAILED, event.getOrderId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("✅ Gửi InventoryFailed thành công cho Order {} tới topic {}", event.getOrderId(), TOPIC_FAILED);
                    } else {
                        log.error("❌ Lỗi gửi InventoryFailed cho Order {}: {}", event.getOrderId(), ex.getMessage());
                    }
                });
    }
}
