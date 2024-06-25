package com.example.finalproject.controller;

import com.example.finalproject.dto.request.category.CreateCategoryRequest;
import com.example.finalproject.dto.request.category.UpdateCategoryRequest;
import com.example.finalproject.dto.response.category.CategoryResponse;
import com.example.finalproject.entity.Category;
import com.example.finalproject.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest){

        CategoryResponse responseBody = categoryService.createCategory(createCategoryRequest);

        return ResponseEntity.ok(responseBody);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Integer id,
                                                   @RequestBody UpdateCategoryRequest updateCategoryRequest){
        Category category = categoryService.updateCategory(id, updateCategoryRequest);

        return ResponseEntity.ok(category);

    }
}
