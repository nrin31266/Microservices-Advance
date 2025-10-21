package com.rin.inventoryservice.repository;

import com.rin.inventoryservice.entity.ReservedOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReservedOrderRepository extends JpaRepository<ReservedOrder, Long> {
    @Modifying
    @Query(value = """
    INSERT INTO reserved_orders (order_id, product_id, quantity)
    VALUES (:orderId, :productId, :quantity)
    ON CONFLICT (order_id, product_id) DO NOTHING
    """, nativeQuery = true)
    void insertIfNotExists(Long orderId, String productId, int quantity);


    Optional<ReservedOrder> findByOrderIdAndProductId(Long orderId, String productId);


    @Modifying
    @Query("DELETE FROM ReservedOrder ro WHERE ro.orderId = :orderId AND ro.productId = :productId")
    void deleteByOrderIdAndProductId(Long orderId, String productId);

}
