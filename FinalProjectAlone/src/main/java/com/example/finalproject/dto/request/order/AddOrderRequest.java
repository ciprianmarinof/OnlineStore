package com.example.finalproject.dto.request.order;

import com.example.finalproject.entity.Product;
import com.example.finalproject.entity.ProductOrder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AddOrderRequest {

    private String title;

    private List<ProductOrder> products;

}
