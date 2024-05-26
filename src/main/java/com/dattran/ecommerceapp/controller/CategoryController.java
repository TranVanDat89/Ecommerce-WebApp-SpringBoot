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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
