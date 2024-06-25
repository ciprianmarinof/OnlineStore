package com.example.finalproject.dto.response.order;

import com.example.finalproject.entity.OrderDetails;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {


    private String title;
    private String status;
    private List<String> orderDetails;
}
