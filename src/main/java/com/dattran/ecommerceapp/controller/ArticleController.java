package com.dattran.ecommerceapp.controller;

import com.dattran.ecommerceapp.dto.ArticleDTO;
import com.dattran.ecommerceapp.dto.response.HttpResponse;
import com.dattran.ecommerceapp.entity.Article;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.service.IArticleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/articles")
public class ArticleController {
    IArticleService articleService;

//    @PostMapping(value = "/create-article", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public HttpResponse createArticle(@RequestBody ArticleDTO articleDTO, HttpServletRequest httpServletRequest) {
//        Article article = articleService.createArticle(articleDTO);
//        HttpResponse httpResponse = HttpResponse.builder()
//                .timeStamp(LocalDateTime.now().toString())
//                .path(httpServletRequest.getRequestURI())
//                .requestMethod(httpServletRequest.getMethod())
//                .status(HttpStatus.OK)
//                .statusCode(ResponseStatus.ARTICLE_CREATED.getCode())
//                .message(ResponseStatus.ARTICLE_CREATED.getMessage())
//                .data(Map.of("article", article))
//                .build();
//        return httpResponse;
//    }

    @PostMapping(value = "/create-article", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HttpResponse createArticle(@RequestParam("title") String title,
                                      @RequestParam("category") String category,
                                      @RequestParam("imageFile") MultipartFile imageFile,
                                      @RequestParam("content") String content, HttpServletRequest httpServletRequest) {
        ArticleDTO articleDTO = ArticleDTO.builder()
                .title(title)
                .category(category)
                .imageFile(imageFile)
                .content(content)
                .build();
        Article article = articleService.createArticle(articleDTO);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.ARTICLE_CREATED.getCode())
                .message(ResponseStatus.ARTICLE_CREATED.getMessage())
                .data(Map.of("article", article))
                .build();
        return httpResponse;
    }

    @GetMapping("/all")
    public HttpResponse getAllArticles(HttpServletRequest httpServletRequest) {
        List<Article> articles = articleService.getAllArticles();
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_ALL_ARTICLE_SUCCESS.getCode())
                .message(ResponseStatus.GET_ALL_ARTICLE_SUCCESS.getMessage())
                .data(Map.of("articles", articles))
                .build();
        return httpResponse;
    }
}
