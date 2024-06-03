package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, String> {
}
