package com.example.finalproject.repository;

import com.example.finalproject.entity.Category;
import com.example.finalproject.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

}
