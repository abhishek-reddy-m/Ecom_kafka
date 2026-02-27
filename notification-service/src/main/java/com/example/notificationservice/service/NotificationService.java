package com.example.notificationservice.service;

import com.example.notificationservice.dto.InventoryReservedEvent;
import com.example.notificationservice.dto.OrderCreatedEvent;
import com.example.notificationservice.dto.PaymentProcessedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void notifyOrderCreated(OrderCreatedEvent event) {
        log.info("[NOTIFICATION] New order placed! " +
                        "orderId={}, userId={}, productId={}, qty={}, amount=${}",
                event.getOrderId(), event.getUserId(),
                event.getProductId(), event.getQuantity(), event.getPrice());
        // In production: send email/SMS/push notification
    }

    public void notifyPaymentProcessed(PaymentProcessedEvent event) {
        if ("SUCCESS".equals(event.getStatus())) {
            log.info("[NOTIFICATION] Payment successful! " +
                            "orderId={}, userId={}, amount=${}, paymentId={}",
                    event.getOrderId(), event.getUserId(),
                    event.getAmount(), event.getPaymentId());
        } else {
            log.warn("[NOTIFICATION] Payment failed! " +
                            "orderId={}, userId={}, amount=${}, reason={}",
                    event.getOrderId(), event.getUserId(),
                    event.getAmount(), event.getFailureReason());
        }
        // In production: send email/SMS/push notification
    }

    public void notifyInventoryReserved(InventoryReservedEvent event) {
        if ("RESERVED".equals(event.getStatus())) {
            log.info("[NOTIFICATION] Inventory reserved! " +
                            "orderId={}, productId={}, qty={}",
                    event.getOrderId(), event.getProductId(), event.getQuantity());
        } else {
            log.warn("[NOTIFICATION] Inventory reservation failed! " +
                            "orderId={}, productId={}, reason={}",
                    event.getOrderId(), event.getProductId(), event.getFailureReason());
        }
        // In production: send email/SMS/push notification
    }
}
