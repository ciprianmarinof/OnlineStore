package com.example.finalproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "order_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"order_id", "product_id"})
})
@ToString(exclude = "order")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderDetailId;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;
}
