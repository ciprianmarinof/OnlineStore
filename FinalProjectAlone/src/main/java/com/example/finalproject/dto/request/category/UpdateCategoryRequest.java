package com.example.finalproject.dto.request.category;

import lombok.Data;

@Data
public class UpdateCategoryRequest {

    private String name;
    private Integer parentId;
}
