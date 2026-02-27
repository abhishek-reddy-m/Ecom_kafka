package com.example.inventoryservice.kafka;

import com.example.inventoryservice.dto.OrderCreatedEvent;
import com.example.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventConsumer.class);

    @Autowired
    private InventoryService inventoryService;

    @KafkaListener(
            topics = "order.created",
            groupId = "inventory-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received order.created event: orderId={}", event.getOrderId());
        inventoryService.reserveInventory(event);
    }
}
