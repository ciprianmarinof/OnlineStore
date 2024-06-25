package com.example.finalproject.service;

import com.example.finalproject.dto.request.category.CreateCategoryRequest;
import com.example.finalproject.dto.request.category.UpdateCategoryRequest;
import com.example.finalproject.dto.response.category.CategoryResponse;
import com.example.finalproject.entity.Category;
import com.example.finalproject.mapper.CategoryMapper;
import com.example.finalproject.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;


    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {

        Category category = categoryMapper.fromCategoryRequest(createCategoryRequest);


        if(createCategoryRequest.getParentId() != null){
            Category parentCategory = categoryRepository.findById(createCategoryRequest.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));

            category.setParent(parentCategory);
        }

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public Category updateCategory(Integer id, UpdateCategoryRequest updateCategoryRequest) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        category.setName(updateCategoryRequest.getName());

        if(updateCategoryRequest.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(updateCategoryRequest.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));

            category.setParent(parentCategory);
        } else {
            category.setParent(null);
        }


        return categoryRepository.save(category);
    }
}
