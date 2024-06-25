package com.example.finalproject.dto.response.category;

import lombok.Data;

@Data
public class CategoryResponse {

    private Integer id;

    private String name;

    private Integer parentId;
}
