package com.example.orderservice.service;

import com.example.orderservice.dto.OrderCreatedEvent;
import com.example.orderservice.entity.Order;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderService {

    public Order createOrder(String userId, String productId, int quantity, double price) {
        Order order = Order.builder()
                .userId(userId)
                .productId(productId)
                .quantity(quantity)
                .price(price)
                .status("CREATED")
                .createdAt(Instant.now())
                .build();

        // Here you would save the order to the database, e.g., orderRepository.save(order);

        return order;
    }

    public OrderCreatedEvent mapToEvent(Order order) {
        return OrderCreatedEvent.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .createdAt(order.getCreatedAt())
                .eventId("event-" + Instant.now().toEpochMilli())
                .eventType("ORDER_CREATED")
                .build();
    }
}
