package com.example.paymentservice.service;

import com.example.paymentservice.dto.OrderCreatedEvent;
import com.example.paymentservice.dto.PaymentProcessedEvent;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.kafka.PaymentEventProducer;
import com.example.paymentservice.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentEventProducer paymentEventProducer;

    public void processPayment(OrderCreatedEvent orderEvent) {
        log.info("Processing payment for orderId={}, userId={}, amount={}",
                orderEvent.getOrderId(), orderEvent.getUserId(), orderEvent.getPrice());

        Payment payment = Payment.builder()
                .orderId(orderEvent.getOrderId())
                .userId(orderEvent.getUserId())
                .amount(orderEvent.getPrice())
                .status(Payment.PaymentStatus.PROCESSING)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        payment = paymentRepository.save(payment);

        // Simulate payment processing: fail if amount > 10000
        boolean paymentSuccess = orderEvent.getPrice() <= 10000;

        if (paymentSuccess) {
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            payment.setUpdatedAt(Instant.now());
            paymentRepository.save(payment);

            PaymentProcessedEvent event = PaymentProcessedEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType("PAYMENT_PROCESSED")
                    .paymentId(payment.getPaymentId())
                    .orderId(orderEvent.getOrderId())
                    .userId(orderEvent.getUserId())
                    .amount(orderEvent.getPrice())
                    .status("SUCCESS")
                    .processedAt(Instant.now())
                    .build();

            paymentEventProducer.sendPaymentEvent(event, "payment.processed");
            log.info("Payment SUCCESS for orderId={}", orderEvent.getOrderId());
        } else {
            String reason = "Amount exceeds maximum allowed limit of 10000";
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason(reason);
            payment.setUpdatedAt(Instant.now());
            paymentRepository.save(payment);

            PaymentProcessedEvent event = PaymentProcessedEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType("PAYMENT_FAILED")
                    .paymentId(payment.getPaymentId())
                    .orderId(orderEvent.getOrderId())
                    .userId(orderEvent.getUserId())
                    .amount(orderEvent.getPrice())
                    .status("FAILED")
                    .failureReason(reason)
                    .processedAt(Instant.now())
                    .build();

            paymentEventProducer.sendPaymentEvent(event, "payment.failed");
            log.warn("Payment FAILED for orderId={}: {}", orderEvent.getOrderId(), reason);
        }
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByUser(String userId) {
        return paymentRepository.findByUserId(userId);
    }
}
