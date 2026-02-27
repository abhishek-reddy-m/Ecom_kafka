package com.example.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    private String productId;

    private String productName;
    private int availableQuantity;
    private int reservedQuantity;

    public boolean hasStock(int requiredQuantity) {
        return availableQuantity >= requiredQuantity;
    }

    public void reserve(int quantity) {
        this.availableQuantity -= quantity;
        this.reservedQuantity += quantity;
    }
}
