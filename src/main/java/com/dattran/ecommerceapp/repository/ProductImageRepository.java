package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, String> {
}
