package com.example.finalproject.entity;

import lombok.Data;

@Data
public class ProductOrder {

    public ProductOrder(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public ProductOrder() {
    }

    private String productName;
    private int quantity;
}
