package com.example.finalproject.mapper;

import com.example.finalproject.dto.request.product.AddProductRequest;
import com.example.finalproject.dto.request.product.UpdateProductRequest;
import com.example.finalproject.dto.response.product.ProductResponse;
import com.example.finalproject.entity.Product;
import com.example.finalproject.repository.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final CategoryRepository categoryRepository;

    public ProductMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse fromProductEntity(Product product){

        ProductResponse response = new ProductResponse();

        response.setName(product.getProductName());
        response.setPrice(product.getProductPrice());
        response.setQuantity(product.getQuantity());

        if(product.getCategory() != null) {
            response.setCategoryName(product.getCategory().getName());
        }

        return response;
    }


    public Product fromProductRequest(AddProductRequest addProductRequest){
        Product product = new Product();

        product.setProductName(addProductRequest.getName());
        product.setProductPrice(addProductRequest.getPrice());
        product.setQuantity(addProductRequest.getQuantity());



        return product;
    }


    public Product fromUpdateProductRequest(Product productTarget, UpdateProductRequest updateProductRequest){

        productTarget.setProductPrice(updateProductRequest.getPrice());
        productTarget.setQuantity(updateProductRequest.getQuantity());

        return productTarget;
    }
}
