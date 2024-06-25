package com.example.finalproject.mapper;

import com.example.finalproject.dto.request.order.AddOrderRequest;
import com.example.finalproject.dto.request.order.UpdateOrderRequest;
import com.example.finalproject.dto.response.order.OrderResponse;
import com.example.finalproject.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse fromOrderEntity(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setTitle(order.getTitle());
        orderResponse.setStatus(order.getOrderStatus().name());

        List<String> productNames = order.getOrderDetails().stream()
                .map(orderDetails -> orderDetails.getProduct().getProductName())
                .collect(Collectors.toList());
        orderResponse.setOrderDetails(productNames);
        return orderResponse;
    }

    public Order fromAddOrderRequest(AddOrderRequest addOrderRequest) {
        Order order = new Order();
        order.setTitle(addOrderRequest.getTitle());
        order.setOrderStatus(Order.OrderStatus.PLACED);


        return order;
    }

    public Order fromUpdateOrderRequest(Order targetOrder, UpdateOrderRequest updateOrderRequest) {


        return targetOrder;
    }
}