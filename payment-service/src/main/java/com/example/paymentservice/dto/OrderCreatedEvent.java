package com.example.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
    private String eventId;
    private String eventType;
    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private double price;
    private Instant createdAt;
}
