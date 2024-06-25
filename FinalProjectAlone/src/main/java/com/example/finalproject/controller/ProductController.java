package com.example.finalproject.controller;


import com.example.finalproject.dto.request.product.AddProductRequest;
import com.example.finalproject.dto.request.product.UpdateProductRequest;
import com.example.finalproject.dto.response.product.ProductResponse;
import com.example.finalproject.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> responseBody = productService.getAllProducts();

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id){

        ProductResponse productResponse = productService.getProductById(id);

        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }


    @PostMapping()
    public ResponseEntity<ProductResponse> addProduct(@RequestBody AddProductRequest addProductRequest){

        ProductResponse productResponse = productService.addProduct(addProductRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Integer id,@RequestBody UpdateProductRequest updateProductRequest){

       productService.updateProduct(id, updateProductRequest);

       return ResponseEntity.status(HttpStatus.OK).build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id){

        productService.deleteProduct(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
