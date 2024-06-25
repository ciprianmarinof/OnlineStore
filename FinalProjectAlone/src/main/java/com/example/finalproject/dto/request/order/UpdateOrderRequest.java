package com.example.finalproject.dto.request.order;

import com.example.finalproject.entity.OrderDetails;
import com.example.finalproject.entity.ProductOrder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
public class UpdateOrderRequest {

    private List<ProductOrder> products;

    @CreationTimestamp
    private Date orderDate;
}
