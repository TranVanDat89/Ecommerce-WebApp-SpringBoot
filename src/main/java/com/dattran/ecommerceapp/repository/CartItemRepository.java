package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByCartIdAndProductId(String cartId, String productId);
}
