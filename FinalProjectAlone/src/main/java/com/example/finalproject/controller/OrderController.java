package com.example.finalproject.controller;

import com.example.finalproject.dto.request.order.AddOrderRequest;
import com.example.finalproject.dto.request.order.UpdateOrderItemQuantityRequest;
import com.example.finalproject.dto.request.order.UpdateOrderRequest;
import com.example.finalproject.dto.response.order.OrderResponse;
import com.example.finalproject.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody AddOrderRequest addOrderRequest){
        OrderResponse responseBody = orderService.createOrder(addOrderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);


    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Integer id){

        OrderResponse orderResponse = orderService.getOrderById(id);

        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> responseBody = orderService.getAllOrders();


        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {

        orderService.deleteOrder(id);


        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Integer id, @RequestBody UpdateOrderRequest updateOrderRequest) {

        OrderResponse orderResponse = orderService.updateOrder(id, updateOrderRequest);

//        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);

        return ResponseEntity.ok(orderResponse);
    }


    @PutMapping("/update-item-quantity")
    public ResponseEntity<OrderResponse> updateQuantitySingleItem(@RequestBody UpdateOrderItemQuantityRequest updateOrderItemQuantityRequest)
    {
        OrderResponse orderResponse = orderService.updateQuantityFromSingleItem(updateOrderItemQuantityRequest);

        return ResponseEntity.ok(orderResponse);
    }

}
