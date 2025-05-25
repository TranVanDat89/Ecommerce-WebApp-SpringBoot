package com.dattran.ecommerceapp.controller;

import com.dattran.ecommerceapp.dto.response.HttpResponse;
import com.dattran.ecommerceapp.dto.response.WishListResponse;
import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.entity.WishList;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.service.IProductService;
import com.dattran.ecommerceapp.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}")
public class FavoriteController {
    SecurityUtil securityUtil;
    IProductService productService;
    @PostMapping("/favorite-products")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public HttpResponse getAllFavorites(HttpServletRequest httpServletRequest) throws Exception {
        User loggedUser = securityUtil.getLoggedInUserInfor();
        List<WishListResponse> wishLists = productService.getAllFavorites(loggedUser.getId());
//        List<WishList> wishLists = productService.getAllFavoriteProducts(loggedUser.getId());
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getMessage())
                .data(Map.of("favorites", wishLists))
                .build();
        return httpResponse;
    }
}
