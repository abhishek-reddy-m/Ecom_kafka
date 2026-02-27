package com.example.paymentservice.kafka;

import com.example.paymentservice.dto.OrderCreatedEvent;
import com.example.paymentservice.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventConsumer.class);

    @Autowired
    private PaymentService paymentService;

    @KafkaListener(
            topics = "order.created",
            groupId = "payment-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received order.created event: orderId={}", event.getOrderId());
        paymentService.processPayment(event);
    }
}
