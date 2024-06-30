package com.example.finalproject.testing;

import com.example.finalproject.dto.request.product.AddProductRequest;
import com.example.finalproject.dto.request.product.UpdateProductRequest;
import com.example.finalproject.dto.response.product.ProductResponse;
import com.example.finalproject.entity.Category;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

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
    public void getAllProducts() {

        List<Product> productList = Arrays.asList(new Product(),new Product());

        List<ProductResponse> productResponseList = Arrays.asList(new ProductResponse(), new ProductResponse());

        when(productRepository.findAll()).thenReturn(productList);
        when(productMapper.fromProductEntity(any(Product.class)))
                .thenAnswer(invocationOnMock -> {
                    Product product = invocationOnMock.getArgument(0);
                    return productResponseList.get(productList.indexOf(product));
                });

        List<ProductResponse> response = productServiceImpl.getAllProducts();

        Assertions.assertEquals(productResponseList,response);
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(productList.size())).fromProductEntity(any(Product.class));
    }

    @Test
    public void addProduct(){
        AddProductRequest addProductRequest = new AddProductRequest();
        addProductRequest.setCategoryName("Electronics");
        addProductRequest.setName("Smartphone");
        addProductRequest.setPrice(999.0);
        addProductRequest.setQuantity(15);

        Product product = new Product();
        Category category = new Category();
        category.setName("Electronics");

        ProductResponse productResponse = new ProductResponse();

        when(productMapper.fromProductRequest(any(AddProductRequest.class))).thenReturn(product);
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.fromProductEntity(any(Product.class))).thenReturn(productResponse);

        ProductResponse response = productServiceImpl.addProduct(addProductRequest);

        Assertions.assertEquals(productResponse, response);

        verify(productMapper, times(1)).fromProductRequest(any(AddProductRequest.class));
        verify(categoryRepository, times(1)).findByName(anyString());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).fromProductEntity(any(Product.class));
    }

    @Test
    public void updateProduct(){

        Integer productId = 1;
        UpdateProductRequest updateProductRequest = new UpdateProductRequest();
        updateProductRequest.setPrice(599.0);
        updateProductRequest.setQuantity(10);

        Product existingProduct = new Product();
        Product updatedProduct = new Product();

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(existingProduct));
        when(productMapper.fromUpdateProductRequest(any(Product.class), any(UpdateProductRequest.class)))
                .thenReturn(updatedProduct);
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        productServiceImpl.updateProduct(productId, updateProductRequest);


        verify(productRepository, times(1)).findById(anyInt());
        verify(productMapper, times(1)).fromUpdateProductRequest(any(Product.class), any(UpdateProductRequest.class));
        verify(productRepository, times(1)).save(any(Product.class));

    }

    @Test
    public void deleteProduct() {

        Integer productId = 1;
        Product product = new Product();

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        productServiceImpl.deleteProduct(productId);


        verify(productRepository, times(1)).findById(anyInt());
        verify(productRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void deleteProductNotFound(){

        Integer productId = 1;


        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productServiceImpl.deleteProduct(productId));

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).deleteById(anyInt());


    }
    @Test
    public void productNotFound(){

        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productServiceImpl.getProductById(anyInt()));

        verify(productRepository, times(1)).findById(anyInt());
    }
}
