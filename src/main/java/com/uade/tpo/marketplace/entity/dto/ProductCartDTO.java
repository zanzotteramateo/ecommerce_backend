package com.uade.tpo.marketplace.entity.dto;

import lombok.Data;

@Data
public class ProductCartDTO {
    private Long id;
    private String name;
    private double price;

    public ProductCartDTO(Long id, String name, double price) {
        this.id = id;
        this.name = name; 
        this.price = price;
    }

}
