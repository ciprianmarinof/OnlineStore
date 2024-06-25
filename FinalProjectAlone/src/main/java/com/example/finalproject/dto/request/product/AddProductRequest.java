package com.example.finalproject.dto.request.product;

import lombok.Data;

@Data
public class AddProductRequest {

    private String name;

    private Double price;


    private Integer quantity;


    private String categoryName;
}
