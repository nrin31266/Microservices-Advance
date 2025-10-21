package com.rin.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
        name = "reserved_orders",
        uniqueConstraints = @UniqueConstraint(columnNames = {"order_id", "product_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservedOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; // Dùng ID tự tăng, dễ debug và xoá sửa

    @Column(name = "order_id", nullable = false)
    Long orderId;

    @Column(name = "product_id", nullable = false)
    String productId;

    @Column(nullable = false)
    int quantity;
}
