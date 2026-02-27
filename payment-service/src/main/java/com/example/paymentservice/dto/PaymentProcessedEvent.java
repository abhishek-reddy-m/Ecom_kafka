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
public class PaymentProcessedEvent {
    private String eventId;
    private String eventType;   // PAYMENT_PROCESSED or PAYMENT_FAILED
    private String paymentId;
    private String orderId;
    private String userId;
    private double amount;
    private String status;      // SUCCESS or FAILED
    private String failureReason;
    private Instant processedAt;
}
