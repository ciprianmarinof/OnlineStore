package com.example.finalproject.service;


import com.example.finalproject.dto.request.category.CreateCategoryRequest;
import com.example.finalproject.dto.request.category.UpdateCategoryRequest;
import com.example.finalproject.dto.response.category.CategoryResponse;
import com.example.finalproject.entity.Category;

public interface CategoryService {


    CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest);


    Category updateCategory(Integer id, UpdateCategoryRequest updateCategoryRequest);


    //implement deleteCategory

    //implement getCategoryById

    //implement getCategoryByName

}
