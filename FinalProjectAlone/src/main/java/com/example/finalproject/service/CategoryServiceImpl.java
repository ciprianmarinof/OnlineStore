package com.example.finalproject.service;

import com.example.finalproject.dto.request.category.CreateCategoryRequest;
import com.example.finalproject.dto.request.category.UpdateCategoryRequest;
import com.example.finalproject.dto.response.category.CategoryResponse;
import com.example.finalproject.entity.Category;
import com.example.finalproject.exception.category.CategoryNotFoundException;
import com.example.finalproject.mapper.CategoryMapper;
import com.example.finalproject.mapper.ProductMapper;
import com.example.finalproject.repository.CategoryRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;



    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, ProductMapper productMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    //for frontend
    public List<Category> getAllCategoriesWithoutParent() {
        return categoryRepository.findByParentIsNull();
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
    @Transactional
    public void deleteCategory(Integer id) {

        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if(categoryOptional.isPresent()){
            Category category = categoryOptional.get();
            categoryRepository.delete(category);
        } else {
            throw new CategoryNotFoundException("Category with id " + id + " not found");
        }
    }

    @Override
    public CategoryResponse getCategoryById(Integer id) {

        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if(categoryOptional.isPresent()){
            Category category = categoryOptional.get();
            CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);
            return categoryResponse;
        } else {
            throw new CategoryNotFoundException("Category not found");
        }
    }

    @Override
    public List<CategoryResponse> getAllCategories() {

        List<Category> categories = categoryRepository.findAll();

        List<CategoryResponse> categoryResponseList = categories.stream().map(categoryMapper::toCategoryResponse).collect(Collectors.toList());

        return categoryResponseList;
    }

    @Override
    @Transactional
    public Category updateCategory(Integer id, UpdateCategoryRequest updateCategoryRequest) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        category.setName(updateCategoryRequest.getName());

        if(updateCategoryRequest.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(updateCategoryRequest.getParentId())
                    .orElseThrow(() -> new CategoryNotFoundException("Parent category not found"));

            category.setParent(parentCategory);
        } else {
            category.setParent(null);
        }


        return categoryRepository.save(category);
    }




}
