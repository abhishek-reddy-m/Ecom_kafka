package com.example.orderservice.service;

import com.example.orderservice.dto.OrderCreatedEvent;
import com.example.orderservice.entity.Order;
import com.example.orderservice.kafka.OrderEventProducer;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderEventProducer orderEventProducer;

    public Order createOrder(String userId, String productId, int quantity, double price) {
        Order order = Order.builder()
                .userId(userId)
                .productId(productId)
                .quantity(quantity)
                .price(price)
                .status("CREATED")
                .createdAt(Instant.now())
                .build();

        Order savedOrder = orderRepository.save(order);
        OrderCreatedEvent event = mapToEvent(savedOrder);
        orderEventProducer.sendOrderCreatedEvent(event);

        return savedOrder;
    }

    private OrderCreatedEvent mapToEvent(Order order) {
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
