package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.entity.ArticleCategory;
import com.dattran.ecommerceapp.entity.Category;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.repository.ArticleCategoryRepository;
import com.dattran.ecommerceapp.repository.CategoryRepository;
import com.dattran.ecommerceapp.service.ICategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements ICategoryService {
    CategoryRepository categoryRepository;
    ArticleCategoryRepository articleCategoryRepository;
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAllByIsDeleted(false);
    }

    @Override
    public Category createCategory(String categoryName, Boolean isArticleCategory) {
        if (isArticleCategory) {
            Optional<ArticleCategory> articleCategory = articleCategoryRepository.findByName(categoryName);
            if (articleCategory.isPresent()) {
                throw new AppException(ResponseStatus.CATEGORY_EXISTED);
            }
            articleCategoryRepository.save(ArticleCategory.builder().name(categoryName).isDeleted(false).build());
            return null;
        }
        Optional<Category> category = categoryRepository.findByName(categoryName);
        if (category.isPresent()) {
            throw new AppException(ResponseStatus.CATEGORY_EXISTED);
        }
        return categoryRepository.save(Category.builder().name(categoryName).isDeleted(false).build());
    }

    @Override
    public void deleteCategory(String id, Boolean isArticleCategory) {
        if (isArticleCategory) {
            ArticleCategory articleCategory = articleCategoryRepository.findById(id)
                    .orElseThrow(()->new AppException(ResponseStatus.CATEGORY_NOT_FOUND));
            articleCategory.setIsDeleted(true);
            articleCategoryRepository.save(articleCategory);
            return;
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new AppException(ResponseStatus.CATEGORY_NOT_FOUND));
        category.setIsDeleted(true);
        categoryRepository.save(category);
    }
}
