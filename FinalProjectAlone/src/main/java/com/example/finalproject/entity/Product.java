package com.example.finalproject.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@ToString(exclude = "orderDetails")
@AllArgsConstructor
@NoArgsConstructor
public class Product {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String productName;

    private Integer quantity;

    private double productPrice;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderDetails> orderDetails;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
