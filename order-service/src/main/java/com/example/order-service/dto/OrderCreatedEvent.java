package com.example.orderservice.dto;

import lombok.*;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent{

    private String eventId;
    private String eventType;
    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private double price;
    private Instant createdAt;
}