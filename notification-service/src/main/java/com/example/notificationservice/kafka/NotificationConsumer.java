package com.example.notificationservice.kafka;

import com.example.notificationservice.dto.InventoryReservedEvent;
import com.example.notificationservice.dto.OrderCreatedEvent;
import com.example.notificationservice.dto.PaymentProcessedEvent;
import com.example.notificationservice.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(
            topics = "order.created",
            groupId = "notification-order-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleOrderCreated(String payload) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(payload, OrderCreatedEvent.class);
            notificationService.notifyOrderCreated(event);
        } catch (Exception e) {
            log.error("Error processing order.created notification: {}", e.getMessage());
        }
    }

    @KafkaListener(
            topics = "payment.processed",
            groupId = "notification-payment-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePaymentProcessed(String payload) {
        try {
            PaymentProcessedEvent event = objectMapper.readValue(payload, PaymentProcessedEvent.class);
            notificationService.notifyPaymentProcessed(event);
        } catch (Exception e) {
            log.error("Error processing payment.processed notification: {}", e.getMessage());
        }
    }

    @KafkaListener(
            topics = "payment.failed",
            groupId = "notification-payment-failed-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePaymentFailed(String payload) {
        try {
            PaymentProcessedEvent event = objectMapper.readValue(payload, PaymentProcessedEvent.class);
            notificationService.notifyPaymentProcessed(event);
        } catch (Exception e) {
            log.error("Error processing payment.failed notification: {}", e.getMessage());
        }
    }

    @KafkaListener(
            topics = "inventory.reserved",
            groupId = "notification-inventory-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleInventoryReserved(String payload) {
        try {
            InventoryReservedEvent event = objectMapper.readValue(payload, InventoryReservedEvent.class);
            notificationService.notifyInventoryReserved(event);
        } catch (Exception e) {
            log.error("Error processing inventory.reserved notification: {}", e.getMessage());
        }
    }

    @KafkaListener(
            topics = "inventory.failed",
            groupId = "notification-inventory-failed-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleInventoryFailed(String payload) {
        try {
            InventoryReservedEvent event = objectMapper.readValue(payload, InventoryReservedEvent.class);
            notificationService.notifyInventoryReserved(event);
        } catch (Exception e) {
            log.error("Error processing inventory.failed notification: {}", e.getMessage());
        }
    }
}
