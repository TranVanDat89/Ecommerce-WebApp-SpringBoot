package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, String> {
}
