package com.example.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
