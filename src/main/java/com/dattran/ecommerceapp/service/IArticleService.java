package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.dto.ArticleDTO;
import com.dattran.ecommerceapp.entity.Article;
import com.dattran.ecommerceapp.entity.ArticleCategory;

import java.util.List;

public interface IArticleService {
    List<Article> getAllArticles();
    Article createArticle(ArticleDTO articleDTO);
    void deleteArticle(String id);
    Article updateArticle(Article article);
    List<ArticleCategory> getAllArticleCategories();
    Article getArticleById(String id);
    List<Article> getAllArticlesByCategoryId(String categoryId);
}
