package com.example.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;

    private String orderId;
    private String userId;
    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String failureReason;
    private Instant createdAt;
    private Instant updatedAt;

    public enum PaymentStatus {
        PROCESSING, SUCCESS, FAILED
    }
}
