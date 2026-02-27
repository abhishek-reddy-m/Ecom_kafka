package com.example.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentProcessedEvent {
    private String eventId;
    private String eventType;
    private String paymentId;
    private String orderId;
    private String userId;
    private double amount;
    private String status;
    private String failureReason;
    private Instant processedAt;
}
