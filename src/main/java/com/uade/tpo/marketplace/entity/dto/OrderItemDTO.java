package com.uade.tpo.marketplace.entity.dto;

import lombok.Data;

@Data

public class OrderItemDTO {
    private String name;
    private double price;
    private int quantity;
    
    public OrderItemDTO(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    
}
