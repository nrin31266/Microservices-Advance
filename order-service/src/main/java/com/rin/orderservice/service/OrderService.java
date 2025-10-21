package com.rin.orderservice.service;


import com.rin.orderservice.entity.Order;
import com.rin.orderservice.entity.OrderStatus;
import com.rin.orderservice.event.OrderCancelledEvent;
import com.rin.orderservice.event.OrderCompletedEvent;
import com.rin.orderservice.event.OrderCreatedEvent;
import com.rin.orderservice.message.producer.OrderEventProducer;
import com.rin.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    OrderEventProducer orderEventProducer;

    public Order createOrder(Order order) {
        // Đặt trạng thái ban đầu
        order.setStatus(OrderStatus.PENDING);
        Order saved = orderRepository.save(order);

        // Tạo event
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(saved.getId())
                .userId(saved.getUserId())
                .productId(saved.getProductId())
                .quantity(saved.getQuantity())
                .total(saved.getTotal())
                .build();

        // Gửi qua producer
        orderEventProducer.publishOrderCreatedEvent(event);

        return saved;
    }

    public void updateOrderStatus(Long orderId, OrderStatus status, String reason) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            Order updated = orderRepository.save(order);
            System.out.println("✅ Đã cập nhật trạng thái đơn hàng: " + status);

            // Nếu Payment xong thì phát event
            if (status == OrderStatus.COMPLETED) {
                OrderCompletedEvent event = new OrderCompletedEvent(
                        updated.getId(),
                        updated.getUserId(),
                        status.name()
                );
                orderEventProducer.publishOrderCompletedEvent(event);
            }

            // Nếu Cancel thì phát event hủy và release stock
            if (status == OrderStatus.CANCELLED) {
                OrderCancelledEvent event = new OrderCancelledEvent(
                        updated.getId(),
                        updated.getUserId(),
                        updated.getProductId(),
                        updated.getQuantity(),
                        reason

                );
                orderEventProducer.publishOrderCancelledEvent(event);
            }

        });
    }
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        updateOrderStatus(orderId, status, "Order cancelled unknown reason");
    }
}