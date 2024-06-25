package com.example.finalproject.service;

import com.example.finalproject.dto.request.order.AddOrderRequest;
import com.example.finalproject.dto.request.order.UpdateOrderItemQuantityRequest;
import com.example.finalproject.dto.request.order.UpdateOrderRequest;
import com.example.finalproject.dto.response.order.OrderResponse;
import com.example.finalproject.entity.*;
import com.example.finalproject.exception.order.OrderNotFoundException;
import com.example.finalproject.exception.product.ProductNotFoundException;
import com.example.finalproject.exception.user.UserNotFoundException;
import com.example.finalproject.mapper.OrderMapper;
import com.example.finalproject.repository.OrderRepository;
import com.example.finalproject.repository.ProductRepository;
import com.example.finalproject.repository.UserRepository;
import com.example.finalproject.service.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<OrderResponse> getAllOrders() {

        List<Order> orders = orderRepository.findAll();

        List<OrderResponse> orderResponseList = orders.stream()
                .map(orderMapper::fromOrderEntity).collect(Collectors.toList());

        return orderResponseList;

    }

    @Override
    public OrderResponse getOrderById(Integer id) {

        Optional<Order> optionalOrder = orderRepository.findById(id);

        if(optionalOrder.isPresent()){
            Order order = optionalOrder.get();

            OrderResponse orderResponse = orderMapper.fromOrderEntity(order);

            return orderResponse;
        }else {
            throw new RuntimeException("Order not found");
        }
    }

    @Transactional
    public OrderResponse createOrder(AddOrderRequest addOrderRequest) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Integer userId = userDetails.getId();

            Order order = orderMapper.fromAddOrderRequest(addOrderRequest);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            order.setUser(user);

            List<String> productsName = addOrderRequest.getProducts().stream()
                    .map(ProductOrder::getProductName)
                    .collect(Collectors.toList());

            List<Product> orderProducts = productRepository.findAll().stream()
                    .filter(product -> productsName.contains(product.getProductName()))
                    .collect(Collectors.toList());

            List<OrderDetails> orderDetailsList = new ArrayList<>();

            double finalPrice = 0;


            order.setTotalPrice(finalPrice);
            for (ProductOrder productOrder : addOrderRequest.getProducts()) {
                String productName = productOrder.getProductName();
                int quantity = productOrder.getQuantity();

                Product product = orderProducts.stream()
                        .filter(p -> p.getProductName().equals(productName))
                        .findFirst()
                        .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productName));

                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setProduct(product);

                if(product.getQuantity() > productOrder.getQuantity()){

                    orderDetails.setQuantity(quantity);
                    product.setQuantity(product.getQuantity() - productOrder.getQuantity());


                }

                finalPrice += product.getProductPrice() * quantity;

                orderDetails.setOrder(order);

                orderDetailsList.add(orderDetails);
            }


            order.setTotalPrice(finalPrice);
            order.setOrderDetails(orderDetailsList);
            orderRepository.save(order);

            return orderMapper.fromOrderEntity(order);

        } catch (IllegalArgumentException e) {
            logger.error("Error creating order: {}", e.getMessage(), e);
            throw e;

        } catch (Exception e) {
            logger.error("Unexpected error creating order", e);
            throw new RuntimeException("Internal Server Error", e);
        }
    }


    @Override
    public OrderResponse updateOrder(Integer id, UpdateOrderRequest updateOrderRequest) {

        try{
            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));


            orderMapper.fromUpdateOrderRequest(existingOrder, updateOrderRequest);

            existingOrder.getOrderDetails().clear();

            List<String> productsName = updateOrderRequest.getProducts().stream()
                    .map(ProductOrder::getProductName)
                    .collect(Collectors.toList());

            List<Product> orderProducts = productRepository.findAll().stream()
                    .filter(product -> productsName.contains(product.getProductName()))
                    .collect(Collectors.toList());
            List<OrderDetails> orderDetailsList = new ArrayList<>();

            for(ProductOrder productOrder : updateOrderRequest.getProducts()) {
                String productName = productOrder.getProductName();
                int quantity = productOrder.getQuantity();

                Product product = orderProducts.stream()
                        .filter(p -> p.getProductName().equals(productName))
                        .findFirst()
                        .orElseThrow(() -> new ProductNotFoundException("Product not found"));

                OrderDetails orderDetails = new OrderDetails();

                orderDetails.setProduct(product);
                orderDetails.setQuantity(quantity);
                orderDetails.setOrder(existingOrder);

                orderDetailsList.add(orderDetails);

            }

            existingOrder.setOrderDetails(orderDetailsList);

            orderRepository.save(existingOrder);

            return orderMapper.fromOrderEntity(existingOrder);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating order: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating order", e);
            throw new RuntimeException("Internal Server Error", e);
        }
    }

    @Override
    public OrderResponse updateQuantityFromSingleItem(UpdateOrderItemQuantityRequest updateOrderItemQuantityRequest) {

        try {
            Order order = orderRepository.findById(updateOrderItemQuantityRequest.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found"));

            Optional<OrderDetails> orderDetailsOptional = order.getOrderDetails().stream()
                    .filter(product -> product.getProduct().getProductName()
                            .equals(updateOrderItemQuantityRequest.getProductName()))
                    .findFirst();

            if(orderDetailsOptional.isPresent()){
                OrderDetails orderDetails = orderDetailsOptional.get();
                orderDetails.setQuantity(updateOrderItemQuantityRequest.getQuantity());
                orderRepository.save(order);

                return orderMapper.fromOrderEntity(order);
            } else {
                throw new ProductNotFoundException("Product not found in order:"
                        + updateOrderItemQuantityRequest.getProductName());
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error", e.getMessage(), e);
            throw e;
        } catch (Exception e){
            logger.error("Unexpected error updating product quantity", e);
            throw new RuntimeException("Server error", e);
        }
    }

    @Override
    @Transactional
    public void deleteOrder(Integer id) {

        try {
            Optional<Order> optionalOrder = orderRepository.findById(id);

            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();

                for(OrderDetails orderDetails : order.getOrderDetails()) {
                    Product product = orderDetails.getProduct();
                    product.setQuantity(product.getQuantity() + orderDetails.getQuantity());
                }

                orderRepository.delete(order);

            } else {
                throw new OrderNotFoundException("Order not found with id: " + id);
            }
        } catch (OrderNotFoundException e){
            logger.error("Error deleting order", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            throw new RuntimeException("Internal Server Error", e);
        }
    }
}
