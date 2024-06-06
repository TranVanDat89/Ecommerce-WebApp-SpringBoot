package com.dattran.ecommerceapp.controller;

import com.dattran.ecommerceapp.dto.response.HttpResponse;
import com.dattran.ecommerceapp.enumeration.OrderStatus;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.service.IArticleService;
import com.dattran.ecommerceapp.service.IOrderService;
import com.dattran.ecommerceapp.service.IProductService;
import com.dattran.ecommerceapp.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("${api.prefix}/admin/analytics")
public class AdminController {
    IProductService productService;
    IOrderService orderService;
    IUserService userService;
    IArticleService articleService;
    @GetMapping("/products")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HttpResponse getAnalytics(HttpServletRequest httpServletRequest) {
        Map<String, Long> result = productService.countByCategory();
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getMessage())
                .data(Map.of("result", result))
                .build();
        return httpResponse;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HttpResponse outComeOfSuccessfullyOrders(@RequestParam("year") int year, HttpServletRequest httpServletRequest) {
//        Double outcome = orderService.calculateOutcomeOfSuccessfulOrders(year);
        Map<String, ?> outcome = orderService.calculateOutcomeOrders(year);
        Map<String, ?> totalUsers = userService.countTotalUsersByYear(year);
        Map<String, ?> totalArticles = articleService.countTotalArticlesByYear(year);
        Map<String, ?> totalOrders = orderService.countOrdersByYear(year);
        List<?> result = List.of(outcome, totalOrders, totalArticles, totalUsers);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getMessage())
                .data(Map.of("result", result))
                .build();
        return httpResponse;
    }
    @GetMapping("/outcome-by-month")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HttpResponse getOutcomeByMonth(@RequestParam("year") int year, HttpServletRequest httpServletRequest) {
        Map<String, Double> result = orderService.getTotalMoneyByStatusAndYearGroupedByMonth(OrderStatus.SUCCESS.getName(), year);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getMessage())
                .data(Map.of("result", result))
                .build();
        return httpResponse;
    }
}
