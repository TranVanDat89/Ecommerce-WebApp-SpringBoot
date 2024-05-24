package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Flavor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlavorRepository extends JpaRepository<Flavor, String> {
    boolean existsByName(String name);
    Optional<Flavor> findByName(String name);
}
