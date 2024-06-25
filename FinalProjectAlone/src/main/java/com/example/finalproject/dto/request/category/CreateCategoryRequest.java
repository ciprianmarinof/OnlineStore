package com.example.finalproject.dto.request.category;

import lombok.Data;

@Data
public class CreateCategoryRequest {

    private String name;

    private Integer parentId;
}
