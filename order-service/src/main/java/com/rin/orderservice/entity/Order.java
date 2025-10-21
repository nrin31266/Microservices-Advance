package com.rin.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double total;
    private Long userId;
    private String product;
    private Double price;
    private int quantity;
    private String productId;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
