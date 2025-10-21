package com.rin.inventoryservice.repository;

import com.rin.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface InventoryRepository extends JpaRepository<Inventory, String> {

    // atomic: select + update in a single query will ensure non-negative stock
    @Modifying
    @Query("UPDATE Inventory i SET i.quantity = i.quantity - :qty, i.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE i.productId = :pid AND i.quantity >= :qty")
    int decreaseStockIfAvailable(String pid, int qty);

    @Modifying
    @Query("UPDATE Inventory i SET i.quantity = i.quantity + :qty, i.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE i.productId = :pid")
    int increaseStock(String pid, int qty);
}
