package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, String> {
    Optional<ArticleCategory> findByName(String name);
    List<ArticleCategory> findAllByIsDeleted(boolean isDeleted);
}
