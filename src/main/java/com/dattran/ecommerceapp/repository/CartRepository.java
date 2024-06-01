package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findBySessionId(String sessionId);
    Optional<Cart> findByUserId(String userId);
}
