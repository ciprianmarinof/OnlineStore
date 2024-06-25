package com.example.finalproject.dto.response.product;

import com.example.finalproject.entity.Product;
import lombok.Data;

@Data
public class ProductResponse {

    private String name;
    private Double price;
    private Integer quantity;
    private String categoryName;
}
