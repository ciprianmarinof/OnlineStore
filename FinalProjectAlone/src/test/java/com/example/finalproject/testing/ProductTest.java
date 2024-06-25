package com.example.finalproject.testing;

import com.example.finalproject.dto.response.product.ProductResponse;
import com.example.finalproject.entity.Product;
import com.example.finalproject.exception.product.ProductNotFoundException;
import com.example.finalproject.mapper.ProductMapper;
import com.example.finalproject.repository.CategoryRepository;
import com.example.finalproject.repository.ProductRepository;
import com.example.finalproject.service.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Test
    public void getProductById() {

        Product product = new Product();
        ProductResponse productResponse = new ProductResponse();


        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(productMapper.fromProductEntity(product)).thenReturn(productResponse);

        ProductResponse response = productServiceImpl.getProductById(anyInt());

        Assertions.assertEquals(productResponse, response);

    }

    @Test
    public void productNotFound(){

        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productServiceImpl.getProductById(anyInt()));
    }
}
