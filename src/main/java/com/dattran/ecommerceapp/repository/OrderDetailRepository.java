package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    OrderDetail findByProductId(String productId);
    Optional<List<OrderDetail>> findByOrderId(String orderId);
}
