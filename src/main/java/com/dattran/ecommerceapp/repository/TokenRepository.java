package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
}
