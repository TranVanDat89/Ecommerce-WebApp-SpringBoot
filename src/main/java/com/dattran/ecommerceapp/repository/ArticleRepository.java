package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Article;
import com.dattran.ecommerceapp.entity.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, String> {
    boolean existsByTitle(String title);
    List<Article> findByCategoryId(String categoryId);
    long count();
    @Query(value = "SELECT COUNT(*) FROM articles a WHERE YEAR(a.created_at) = :year", nativeQuery = true)
    long countArticlesByYear(@Param("year") int year);
}
