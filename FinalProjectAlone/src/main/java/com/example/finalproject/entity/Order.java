package com.example.finalproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
@ToString(exclude = "client")
public class Order {

    public enum OrderStatus {
        PLACED,
        PENDING,
        FULFILLED,
        IN_ERROR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;   // UUID

//    private Integer customerOrderId;

    private String title;

    private Double totalPrice;

    // + delivery address

    @CreationTimestamp
    private Date orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetails> orderDetails = new ArrayList<>();

}
