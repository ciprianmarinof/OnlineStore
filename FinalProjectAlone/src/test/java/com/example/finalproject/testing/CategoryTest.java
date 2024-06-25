package com.example.finalproject.testing;

import com.example.finalproject.dto.request.category.CreateCategoryRequest;
import com.example.finalproject.entity.Category;
import com.example.finalproject.repository.CategoryRepository;
import com.example.finalproject.service.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;


    @Test
    public void createCategory(){

        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.setName("IT");
        createCategoryRequest.setParentId(1);

        Category parentCategory = new Category();
        parentCategory.setId(1);
        parentCategory.setName("Electronics");

        Category category = new Category();
        category.setId(2);
        category.setName("IT");
        category.setParent(parentCategory);

        when(categoryRepository.findById(1)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);


        Assertions.assertEquals(category, categoryService.createCategory(createCategoryRequest));
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}
