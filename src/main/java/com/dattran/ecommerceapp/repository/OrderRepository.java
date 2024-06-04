package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserId(String userId);
}
