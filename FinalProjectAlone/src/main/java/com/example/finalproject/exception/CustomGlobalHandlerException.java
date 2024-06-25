package com.example.finalproject.exception;

import com.example.finalproject.exception.category.CategoryNotFoundException;
import com.example.finalproject.exception.order.OrderNotFoundException;
import com.example.finalproject.exception.product.ProductNotFoundException;
import com.example.finalproject.exception.user.UserNotFoundException;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomGlobalHandlerException extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException exception, HttpServletRequest request){

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFoundException(ProductNotFoundException exception, HttpServletRequest request){

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFoundException(ProductNotFoundException exception, HttpServletRequest request){

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(ProductNotFoundException exception, HttpServletRequest request){

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(apiError);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) request).getRequest();
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_ACCEPTABLE)
                .message("Message not accepted")
                .path(httpServletRequest.getRequestURI())
                .build();

        return buildResponseEntity(apiError);
    }

}
