package com.example.finalproject.controller;

import com.example.finalproject.dto.request.category.CreateCategoryRequest;
import com.example.finalproject.dto.request.category.UpdateCategoryRequest;
import com.example.finalproject.dto.response.category.CategoryResponse;
import com.example.finalproject.entity.Category;
import com.example.finalproject.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer id) {

        CategoryResponse categoryResponse = categoryService.getCategoryById(id);

        return ResponseEntity.status(HttpStatus.OK).body(categoryResponse);
    }

    @GetMapping()
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {

        List<CategoryResponse> responseBody = categoryService.getAllCategories();

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        //has orphan removal -> removes products && child categories
        categoryService.deleteCategory(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
