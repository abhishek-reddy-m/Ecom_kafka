package com.example.orderservice.kafka;

import com.example.orderservice.dto.OrderCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class OrderEventProducer {

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        // Example placeholder: you would actually send to Kafka here
        System.out.println("Sending order event: " + event.getOrderId());
    }
}
