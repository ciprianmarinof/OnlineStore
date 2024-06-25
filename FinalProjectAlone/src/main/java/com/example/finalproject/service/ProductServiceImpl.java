package com.example.finalproject.service;

import com.example.finalproject.dto.request.product.AddProductRequest;
import com.example.finalproject.dto.request.product.UpdateProductRequest;
import com.example.finalproject.dto.response.product.ProductResponse;
import com.example.finalproject.entity.Category;
import com.example.finalproject.entity.Product;
import com.example.finalproject.exception.category.CategoryNotFoundException;
import com.example.finalproject.exception.product.ProductNotFoundException;
import com.example.finalproject.mapper.ProductMapper;
import com.example.finalproject.repository.CategoryRepository;
import com.example.finalproject.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        List<Product> products = productRepository.findAll();

        List<ProductResponse> productResponseList = products.stream().map(productMapper::fromProductEntity).collect(Collectors.toList());


        return productResponseList;
    }

    @Override
    public ProductResponse getProductById(Integer id) {

        Optional<Product> productOptional = productRepository.findById(id);

        if(productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductResponse productResponse = productMapper.fromProductEntity(product);
            return productResponse;
        } else {
            throw new ProductNotFoundException("Product not found");
        }
    }

    @Override
    public ProductResponse addProduct(AddProductRequest addProductRequest) {

        Product product = productMapper.fromProductRequest(addProductRequest);

        Optional<Category> optionalCategory = categoryRepository.findByName(addProductRequest.getCategoryName());
        if (optionalCategory.isPresent()) {
            product.setCategory(optionalCategory.get());
        } else throw new CategoryNotFoundException("Category does not exist");

        productRepository.save(product);

        ProductResponse productResponse = productMapper.fromProductEntity(product);

        return productResponse;
    }

    @Override
    public void updateProduct(Integer id, UpdateProductRequest updateProductRequest) {

        Optional<Product> productOptional = productRepository.findById(id);

        if(productOptional.isPresent()){
            Product product = productOptional.get();

            Product productToBeUpdated = productMapper.fromUpdateProductRequest(product, updateProductRequest);

            productRepository.save(productToBeUpdated);
        } else{
            throw new ProductNotFoundException("Product not found");
        }

    }

    @Override
    public void deleteProduct(Integer id) {

        Optional<Product> productOptional = productRepository.findById(id);

        if(productOptional.isPresent()){
//            Product product = productOptional.get(); -> check
            productRepository.deleteById(id);
        } else{
            throw new ProductNotFoundException("Product not found");
        }

    }
}
