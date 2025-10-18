package com.rin.inventoryservice.service;

import com.rin.inventoryservice.entity.Inventory;
import com.rin.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class InventoryService {
    InventoryRepository inventoryRepository;

    // CRUD methods can be added here
    public Inventory findById(String id) {
        return inventoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Inventory not found with id: " + id)
        );
    }
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public void deleteById(String id) {
        inventoryRepository.deleteById(id);
    }

    public Inventory update(Inventory inventory, String id) {
        Inventory existingInventory = findById(id);
        existingInventory.setQuantity(inventory.getQuantity());
        return inventoryRepository.save(existingInventory);
    }
}
