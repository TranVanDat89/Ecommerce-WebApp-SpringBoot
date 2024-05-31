package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.aws.S3Service;
import com.dattran.ecommerceapp.dto.ArticleDTO;
import com.dattran.ecommerceapp.entity.Article;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
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
        Article article = Article.builder()
                .title(articleDTO.getTitle())
                .category(articleDTO.getCategory())
                .content(articleDTO.getContent())
                .imageUrl(imageUrl.isEmpty() ? null : imageUrl)
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
}
