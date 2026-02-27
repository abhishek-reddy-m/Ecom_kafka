package com.example.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryReservedEvent {
    private String eventId;
    private String eventType;   // INVENTORY_RESERVED or INVENTORY_FAILED
    private String orderId;
    private String productId;
    private int quantity;
    private String status;      // RESERVED or FAILED
    private String failureReason;
    private Instant reservedAt;
}
