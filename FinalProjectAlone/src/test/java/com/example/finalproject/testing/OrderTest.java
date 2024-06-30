package com.example.finalproject.testing;

import com.example.finalproject.dto.request.order.AddOrderRequest;
import com.example.finalproject.dto.request.order.UpdateOrderRequest;
import com.example.finalproject.dto.response.order.OrderResponse;
import com.example.finalproject.entity.*;
import com.example.finalproject.exception.order.OrderNotFoundException;
import com.example.finalproject.mapper.OrderMapper;
import com.example.finalproject.repository.OrderRepository;
import com.example.finalproject.repository.ProductRepository;
import com.example.finalproject.repository.UserRepository;
import com.example.finalproject.service.OrderServiceImpl;
import com.example.finalproject.service.security.UserDetailsImpl;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;


    @InjectMocks
    private OrderServiceImpl orderServiceImpl;


    @Test
    public void getAllOrders() {

        List<Order> orderList = Arrays.asList(new Order(), new Order());

        List<OrderResponse> orderResponseList = Arrays.asList(new OrderResponse(), new OrderResponse());

        when(orderRepository.findAll()).thenReturn(orderList);
        when(orderMapper.fromOrderEntity(any(Order.class)))
                .thenAnswer(invocationOnMock -> {
                    Order order = invocationOnMock.getArgument(0);
                    return orderResponseList.get(orderList.indexOf(order));
                });

        List<OrderResponse> response = orderServiceImpl.getAllOrders();


        Assertions.assertEquals(orderResponseList, response);

        verify(orderRepository, times(1)).findAll();
        verify(orderMapper,times(orderList.size())).fromOrderEntity(any(Order.class));
    }




    @Test
    public void deleteById(){

        Order order = new Order();

        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));

        orderServiceImpl.deleteOrder(anyInt());

        verify(orderRepository, times(1)).delete(order);
    }


    @Test
    public void getOrderById(){

        Order order = new Order();
        OrderResponse orderResponse = new OrderResponse();

        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
        when(orderMapper.fromOrderEntity(order)).thenReturn(orderResponse);

        OrderResponse response = orderServiceImpl.getOrderById(anyInt());

        Assertions.assertEquals(orderResponse, response);

    }

    @Test
    public void createOrder(){

        AddOrderRequest addOrderRequest = new AddOrderRequest();
        List<ProductOrder> productOrderList = new ArrayList<>();
        productOrderList.add(new ProductOrder("Product1", 2)); //created constructor for ProductOrder - cascade problems?
        addOrderRequest.setProducts(productOrderList);

        User user = new User();
        user.setId(1);

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        Order order = new Order();

        Product product = new Product();
        product.setProductName("Product1");
        product.setQuantity(10);
        product.setProductPrice(199.0);

        List<Product> productList = new ArrayList<>();
        productList.add(product);

        OrderResponse orderResponse = new OrderResponse();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(productRepository.findAll()).thenReturn(productList);
        when(orderMapper.fromAddOrderRequest(any(AddOrderRequest.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.fromOrderEntity(any(Order.class))).thenReturn(orderResponse);

        OrderResponse response = orderServiceImpl.createOrder(addOrderRequest);

        Assertions.assertEquals(orderResponse,response);

        verify(userRepository, times(1)).findById(anyInt());
        verify(productRepository, times(1)).findAll();
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderMapper, times(1)).fromAddOrderRequest(any(AddOrderRequest.class));
        verify(orderMapper, times(1)).fromOrderEntity(any(Order.class));

    }

    @Test
    public void updateOrder(){

        Integer orderId = 1;
        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest();

        ProductOrder productOrder1 = new ProductOrder("Product1", 2);
        ProductOrder productOrder2 = new ProductOrder("Product2",3);
        updateOrderRequest.setProducts(Arrays.asList(productOrder1,productOrder2));

        Order existingOrder = new Order();
        existingOrder.setId(orderId);

        Product product1 = new Product();
        product1.setProductName("Product1");
        product1.setQuantity(10);

        Product product2 = new Product();
        product2.setProductName("Product2");
        product2.setQuantity(20);

        List<Product> products = Arrays.asList(product1,product2);

        OrderDetails orderDetails1 = new OrderDetails();
        orderDetails1.setProduct(product1);
        orderDetails1.setQuantity(2);

        OrderDetails orderDetails2 = new OrderDetails();
        orderDetails2.setProduct(product2);
        orderDetails2.setQuantity(3);

        List<OrderDetails> orderDetailsList = Arrays.asList(orderDetails1,orderDetails2);

        OrderResponse orderResponse = new OrderResponse();

        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(existingOrder));
        when(productRepository.findAll()).thenReturn(products);
        when(orderMapper.fromUpdateOrderRequest(any(Order.class), any(UpdateOrderRequest.class))).thenReturn(existingOrder);
        when(orderMapper.fromOrderEntity(any(Order.class))).thenReturn(orderResponse);

        OrderResponse response = orderServiceImpl.updateOrder(orderId, updateOrderRequest);

        Assertions.assertNotNull(response);
        verify(orderRepository,times(1)).findById(orderId);
        verify(productRepository,times(1)).findAll();
        verify(orderRepository,times(1)).save(existingOrder);


    }
    @Test
    public void deleteOrderNotFound() {

        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderServiceImpl.deleteOrder(anyInt()));
    }
}
