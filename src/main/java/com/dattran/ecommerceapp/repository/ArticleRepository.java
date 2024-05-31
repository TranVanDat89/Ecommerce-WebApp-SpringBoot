package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String> {
    boolean existsByTitle(String title);
}
