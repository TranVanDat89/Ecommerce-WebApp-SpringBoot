package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}
