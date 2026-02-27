package com.example.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryReservedEvent {
    private String eventId;
    private String eventType;
    private String orderId;
    private String productId;
    private int quantity;
    private String status;
    private String failureReason;
    private Instant reservedAt;
}
