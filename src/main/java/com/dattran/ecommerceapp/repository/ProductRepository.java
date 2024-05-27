package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsByName(String name);
    List<Product> findTop4ByOrderByCreatedAtDesc();
//    @Query("SELECT p FROM Product p WHERE " +
//            "(:categoryId IS NULL OR p.category.id = :categoryId) " +
//            "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword%)")
//    Page<Product> searchProducts
//            (@Param("categoryId") String categoryId,
//             @Param("keyword") String keyword, Pageable pageable);
}
