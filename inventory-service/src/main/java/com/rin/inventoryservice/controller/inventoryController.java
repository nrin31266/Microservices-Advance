package com.rin.inventoryservice.controller;

import com.rin.inventoryservice.entity.Inventory;
import com.rin.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class inventoryController {
    InventoryService inventoryService;

    @GetMapping
    public List<Inventory> findAll() {
        return inventoryService.findAll();
    }

    @GetMapping("/{id}")
    public Inventory findById(@PathVariable String id) {
        return inventoryService.findById(id);
    }

    @PostMapping
    public Inventory save(@RequestBody Inventory inventory) {
        return inventoryService.save(inventory);
    }

    @PutMapping("/{id}")
    public Inventory update(@RequestBody Inventory inventory, @PathVariable String id) {
        return inventoryService.update(inventory, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        inventoryService.deleteById(id);
    }

}
