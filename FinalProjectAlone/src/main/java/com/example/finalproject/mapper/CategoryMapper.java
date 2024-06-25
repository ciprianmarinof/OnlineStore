package com.example.finalproject.mapper;

import com.example.finalproject.dto.request.category.CreateCategoryRequest;
import com.example.finalproject.dto.response.category.CategoryResponse;
import com.example.finalproject.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {


    public Category fromCategoryRequest (CreateCategoryRequest createCategoryRequest) {

        Category category = new Category();
        category.setName(createCategoryRequest.getName());

        return category;
    }


    public CategoryResponse toCategoryResponse(Category category) {

        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setParentId(category.getParent() != null ? category.getParent().getId() : null);

        return response;
    }
}
