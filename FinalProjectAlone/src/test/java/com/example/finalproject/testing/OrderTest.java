package com.example.finalproject.testing;

import com.example.finalproject.entity.Order;
import com.example.finalproject.exception.order.OrderNotFoundException;
import com.example.finalproject.repository.OrderRepository;
import com.example.finalproject.service.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void deleteById(){

        Order order = new Order();

        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));

        orderService.deleteOrder(anyInt());

        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    public void deleteOrderNotFound() {

        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(anyInt()));
    }
}
