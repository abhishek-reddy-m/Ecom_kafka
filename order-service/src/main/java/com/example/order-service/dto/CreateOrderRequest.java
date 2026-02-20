package com.example.orderservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    private String userId;
    private String productId;
    private int quantity;
    private double price;
}
