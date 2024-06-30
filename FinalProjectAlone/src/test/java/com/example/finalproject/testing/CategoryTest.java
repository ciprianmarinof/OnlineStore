package com.example.finalproject.testing;

import com.example.finalproject.dto.request.category.CreateCategoryRequest;
import com.example.finalproject.dto.request.category.UpdateCategoryRequest;
import com.example.finalproject.dto.response.category.CategoryResponse;
import com.example.finalproject.entity.Category;
import com.example.finalproject.mapper.CategoryMapper;
import com.example.finalproject.repository.CategoryRepository;
import com.example.finalproject.service.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;


    @Test
    public void createCategoryWithParent(){

        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.setParentId(1);
        createCategoryRequest.setName("Electronics");

        Category parentCategory = new Category();
        parentCategory.setId(1);


        Category category = new Category();
        category.setName("Electronics");

        Category savedCategory = new Category();
        savedCategory.setId(2);
        savedCategory.setName("Electronics");
        savedCategory.setParent(parentCategory);

        CategoryResponse categoryResponse = new CategoryResponse();

        when(categoryMapper.fromCategoryRequest(any(CreateCategoryRequest.class))).thenReturn(category);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        when(categoryMapper.toCategoryResponse(any(Category.class))).thenReturn(categoryResponse);

        CategoryResponse response = categoryServiceImpl.createCategory(createCategoryRequest);

        Assertions.assertEquals(categoryResponse, response);
        verify(categoryMapper,times(1)).fromCategoryRequest(any(CreateCategoryRequest.class));
        verify(categoryRepository, times(1)).findById(anyInt());
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryMapper, times(1)).toCategoryResponse(any(Category.class));

    }

    @Test
    public void createCategoryWithoutParent() {
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.setName("Electronics");

        Category category = new Category();
        category.setName("Electronics");

        Category savedCategory = new Category();
        savedCategory.setId(2);
        savedCategory.setName("Electronics");

        CategoryResponse categoryResponse = new CategoryResponse();

        when(categoryMapper.fromCategoryRequest(any(CreateCategoryRequest.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        when(categoryMapper.toCategoryResponse(any(Category.class))).thenReturn(categoryResponse);

        CategoryResponse response = categoryServiceImpl.createCategory(createCategoryRequest);

        Assertions.assertEquals(categoryResponse, response);
        verify(categoryMapper, times(1)).fromCategoryRequest(any(CreateCategoryRequest.class));
        verify(categoryRepository, never()).findById(anyInt());
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryMapper, times(1)).toCategoryResponse(any(Category.class));


    }

    @Test
    public void updateCategory(){

        Integer categoryId = 1;
        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest();
        updateCategoryRequest.setName("New Category");
        updateCategoryRequest.setParentId(2);

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Old Category");

        Category parentCategory = new Category();
        parentCategory.setId(2);

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName("New Category");
        updatedCategory.setParent(parentCategory);


        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.findById(updateCategoryRequest.getParentId())).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);

        Category response = categoryServiceImpl.updateCategory(categoryId, updateCategoryRequest);

        Assertions.assertEquals(updatedCategory,response);
        verify(categoryRepository, times(2)).findById(anyInt());
        verify(categoryRepository, times(1)).save(any(Category.class));


    }

    @Test
    public void deleteCategory(){

        Integer categoryId = 1;

        Category category = new Category();

        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));

        categoryServiceImpl.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).findById(anyInt());
        verify(categoryRepository, times(1)).delete(category);


    }


    @Test
    public void getCategoryById() {

        Category category = new Category();
        CategoryResponse categoryResponse = new CategoryResponse();

        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryResponse(category)).thenReturn(categoryResponse);

        CategoryResponse response = categoryServiceImpl.getCategoryById(anyInt());

        Assertions.assertEquals(categoryResponse, response);
    }

    @Test
    public void getAllCategories(){

        Category category1 = new Category();
        category1.setId(1);
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("Category 2");

        List<Category> categories = Arrays.asList(category1, category2);

        CategoryResponse categoryResponse1 = new CategoryResponse();
        categoryResponse1.setId(1);
        categoryResponse1.setName("Category 1");

        CategoryResponse categoryResponse2 = new CategoryResponse();
        categoryResponse2.setId(2);
        categoryResponse2.setName("Category 2");

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toCategoryResponse(category1)).thenReturn(categoryResponse1);
        when(categoryMapper.toCategoryResponse(category2)).thenReturn(categoryResponse2);

        List<CategoryResponse> categoryResponseList = categoryServiceImpl.getAllCategories();

        Assertions.assertNotNull(categoryResponseList);
        Assertions.assertEquals(2, categoryResponseList.size());
        Assertions.assertEquals("Category 1", categoryResponseList.get(0).getName());
        Assertions.assertEquals("Category 2", categoryResponseList.get(1).getName());

        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper,times(1)).toCategoryResponse(category1);
        verify(categoryMapper,times(1)).toCategoryResponse(category2);
    }

}
