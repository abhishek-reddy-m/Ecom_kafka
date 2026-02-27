package com.example.inventoryservice.kafka;

import com.example.inventoryservice.dto.InventoryReservedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class InventoryEventProducer {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventProducer.class);

    @Autowired
    private KafkaTemplate<String, InventoryReservedEvent> kafkaTemplate;

    public void sendInventoryEvent(InventoryReservedEvent event, String topic) {
        CompletableFuture<SendResult<String, InventoryReservedEvent>> future =
                kafkaTemplate.send(topic, event.getOrderId(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Inventory event sent to topic={}: orderId={}, offset={}",
                        topic, event.getOrderId(), result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send inventory event to topic={} for orderId={}",
                        topic, event.getOrderId(), ex);
            }
        });
    }
}
