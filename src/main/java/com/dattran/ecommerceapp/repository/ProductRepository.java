package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsByName(String name);
}
