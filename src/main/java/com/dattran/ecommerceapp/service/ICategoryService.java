package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.entity.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    Category createCategory(String categoryName, Boolean isArticleCategory);
    void deleteCategory(String id, Boolean isArticleCategory);
}
