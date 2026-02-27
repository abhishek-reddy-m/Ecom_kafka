package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryReservedEvent;
import com.example.inventoryservice.dto.OrderCreatedEvent;
import com.example.inventoryservice.entity.InventoryItem;
import com.example.inventoryservice.kafka.InventoryEventProducer;
import com.example.inventoryservice.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryEventProducer inventoryEventProducer;

    @Transactional
    public void reserveInventory(OrderCreatedEvent orderEvent) {
        log.info("Reserving inventory for orderId={}, productId={}, quantity={}",
                orderEvent.getOrderId(), orderEvent.getProductId(), orderEvent.getQuantity());

        Optional<InventoryItem> itemOpt = inventoryRepository.findById(orderEvent.getProductId());

        if (itemOpt.isEmpty()) {
            String reason = "Product not found: " + orderEvent.getProductId();
            publishFailedEvent(orderEvent, reason);
            return;
        }

        InventoryItem item = itemOpt.get();

        if (!item.hasStock(orderEvent.getQuantity())) {
            String reason = String.format("Insufficient stock for productId=%s. Available=%d, Requested=%d",
                    orderEvent.getProductId(), item.getAvailableQuantity(), orderEvent.getQuantity());
            publishFailedEvent(orderEvent, reason);
            return;
        }

        item.reserve(orderEvent.getQuantity());
        inventoryRepository.save(item);

        InventoryReservedEvent event = InventoryReservedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("INVENTORY_RESERVED")
                .orderId(orderEvent.getOrderId())
                .productId(orderEvent.getProductId())
                .quantity(orderEvent.getQuantity())
                .status("RESERVED")
                .reservedAt(Instant.now())
                .build();

        inventoryEventProducer.sendInventoryEvent(event, "inventory.reserved");
        log.info("Inventory RESERVED for orderId={}: productId={}, qty={}",
                orderEvent.getOrderId(), orderEvent.getProductId(), orderEvent.getQuantity());
    }

    private void publishFailedEvent(OrderCreatedEvent orderEvent, String reason) {
        log.warn("Inventory reservation FAILED for orderId={}: {}", orderEvent.getOrderId(), reason);

        InventoryReservedEvent event = InventoryReservedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("INVENTORY_FAILED")
                .orderId(orderEvent.getOrderId())
                .productId(orderEvent.getProductId())
                .quantity(orderEvent.getQuantity())
                .status("FAILED")
                .failureReason(reason)
                .reservedAt(Instant.now())
                .build();

        inventoryEventProducer.sendInventoryEvent(event, "inventory.failed");
    }

    public InventoryItem addStock(String productId, String productName, int quantity) {
        InventoryItem item = inventoryRepository.findById(productId)
                .orElse(InventoryItem.builder()
                        .productId(productId)
                        .productName(productName)
                        .availableQuantity(0)
                        .reservedQuantity(0)
                        .build());

        item.setAvailableQuantity(item.getAvailableQuantity() + quantity);
        if (productName != null) {
            item.setProductName(productName);
        }
        return inventoryRepository.save(item);
    }

    public List<InventoryItem> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Optional<InventoryItem> getInventoryByProductId(String productId) {
        return inventoryRepository.findById(productId);
    }
}
