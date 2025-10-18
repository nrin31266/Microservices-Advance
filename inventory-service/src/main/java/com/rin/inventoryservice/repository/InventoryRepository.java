package com.rin.inventoryservice.repository;

import com.rin.inventoryservice.entity.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
}
