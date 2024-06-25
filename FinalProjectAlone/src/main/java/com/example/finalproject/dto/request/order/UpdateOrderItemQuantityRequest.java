package com.example.finalproject.dto.request.order;

import lombok.Data;

@Data
public class UpdateOrderItemQuantityRequest {

    private Integer orderId;
    private String productName;
    private int quantity;
}
