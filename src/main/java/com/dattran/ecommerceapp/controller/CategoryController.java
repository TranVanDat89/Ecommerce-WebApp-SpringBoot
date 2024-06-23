package com.dattran.ecommerceapp.controller;

import com.dattran.ecommerceapp.dto.response.HttpResponse;
import com.dattran.ecommerceapp.entity.Category;
import com.dattran.ecommerceapp.entity.Product;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.service.ICategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    ICategoryService categoryService;

    @GetMapping("")
    public HttpResponse getAllCategories(HttpServletRequest httpServletRequest) {
        List<Category> categories = categoryService.getAllCategories();
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_ALL_CATEGORIES_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_ALL_CATEGORIES_SUCCESSFULLY.getMessage())
                .data(Map.of("categories", categories))
                .build();
        return httpResponse;
    }

    @PostMapping("")
    public HttpResponse createCategory(@RequestBody String categoryName, @RequestParam Boolean isArticleCategory, HttpServletRequest httpServletRequest) {
        Category newCategory = categoryService.createCategory(categoryName, isArticleCategory);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .data(Map.of("category", newCategory))
                .build();
        return httpResponse;
    }

    @DeleteMapping("/{id}")
    public HttpResponse deleteCategory(@PathVariable String id, @RequestParam boolean isArticleCategory, HttpServletRequest httpServletRequest) {
        categoryService.deleteCategory(id, isArticleCategory);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .build();
        return httpResponse;
    }
}
