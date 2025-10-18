package com.rin.inventoryservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "inventory")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Inventory {
    @Id
    String productId;
    int quantity;
}
