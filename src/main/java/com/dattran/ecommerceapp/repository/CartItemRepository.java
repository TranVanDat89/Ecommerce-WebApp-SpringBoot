package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
}
