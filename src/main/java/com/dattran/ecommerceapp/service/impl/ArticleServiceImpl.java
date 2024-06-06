package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.aws.S3Service;
import com.dattran.ecommerceapp.dto.ArticleDTO;
import com.dattran.ecommerceapp.entity.Article;
import com.dattran.ecommerceapp.entity.ArticleCategory;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.repository.ArticleCategoryRepository;
import com.dattran.ecommerceapp.repository.ArticleRepository;
import com.dattran.ecommerceapp.service.IArticleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ArticleServiceImpl implements IArticleService {
    ArticleRepository articleRepository;
    ArticleCategoryRepository articleCategoryRepository;
    S3Service s3Service;
    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Article createArticle(ArticleDTO articleDTO) {
        String imageUrl = "";
        if (articleDTO.getImageFile() != null) {
             imageUrl =  s3Service.uploadFile(articleDTO.getImageFile(), "articles/");
        }
        if (articleDTO.getContent() == null || articleDTO.getContent().length() <300) {
            throw new AppException(ResponseStatus.ARTICLE_INVALID);
        }
        if (articleRepository.existsByTitle(articleDTO.getTitle())) {
            throw new AppException(ResponseStatus.ARTICLE_NAME_EXISTED);
        }
        ArticleCategory articleCategory = articleCategoryRepository.findById(articleDTO.getArticleCategoryId())
                .orElseThrow(()->new AppException(ResponseStatus.ARTICLE_CATEGORY_NOT_FOUND));
        Article article = Article.builder()
                .title(articleDTO.getTitle())
                .content(articleDTO.getContent())
                .imageUrl(imageUrl.isEmpty() ? null : imageUrl)
                .category(articleCategory)
                .build();
        return articleRepository.save(article);
    }

    @Override
    public void deleteArticle(String id) {

    }

    @Override
    public Article updateArticle(Article article) {
        return null;
    }

    @Override
    public List<ArticleCategory> getAllArticleCategories() {
        return articleCategoryRepository.findAll();
    }

    @Override
    public Article getArticleById(String articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new AppException(ResponseStatus.ARTICLE_NOT_FOUND));
    }

    @Override
    public List<Article> getAllArticlesByCategoryId(String categoryId) {
        ArticleCategory articleCategory = articleCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ResponseStatus.ARTICLE_CATEGORY_NOT_FOUND));
        List<Article> articles = articleRepository.findByCategoryId(categoryId);
//        articles = articles.stream()
//                .filter(article -> article.getCategory().getId().equals(categoryId))
//                .toList();
        return !articles.isEmpty() ? articles : List.of();
    }

    @Override
    public long countTotalArticles() {
        return articleRepository.count();
    }

    @Override
    public long countTotalArticlesByYear(int year) {
        return articleRepository.countArticlesByYear(year);
    }
}
