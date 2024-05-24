package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, String> {
}
