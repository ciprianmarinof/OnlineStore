package com.example.finalproject.service;

import com.example.finalproject.dto.request.order.AddOrderRequest;
import com.example.finalproject.dto.request.order.UpdateOrderItemQuantityRequest;
import com.example.finalproject.dto.request.order.UpdateOrderRequest;
import com.example.finalproject.dto.response.order.OrderResponse;

import java.util.List;

public interface OrderService {

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Integer id);

    OrderResponse createOrder(AddOrderRequest addOrderRequest);

    OrderResponse updateOrder(Integer id, UpdateOrderRequest updateOrderRequest);

    void deleteOrder(Integer id);

    OrderResponse updateQuantityFromSingleItem(UpdateOrderItemQuantityRequest updateOrderItemQuantityRequest);
}
