package com.dattran.ecommerceapp.controller;

import com.dattran.ecommerceapp.dto.response.HttpResponse;
import com.dattran.ecommerceapp.entity.Cart;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.service.ICartService;
import com.dattran.ecommerceapp.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/carts")
public class CartController {
    ICartService cartService;
    SecurityUtil securityUtil;
    @PostMapping("/add-to-cart")
    public HttpResponse addToCart(@RequestParam String userId,
                                  @RequestParam String productId, @RequestParam int quantity,
                                  @RequestParam String flavorName,
                                  HttpServletRequest httpServletRequest) {
        Cart cart = cartService.getOrCreateCart(userId);
        cart = cartService.addItemToCart(cart.getId(), productId, quantity, flavorName);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.ARTICLE_CREATED.getCode())
                .message(ResponseStatus.ARTICLE_CREATED.getMessage())
                .data(Map.of("cart", cart))
                .build();
        return httpResponse;
    }

    @GetMapping("/my-cart")
    public HttpResponse getCart(@RequestParam(required = false) String userId,
                                 HttpServletRequest httpServletRequest) {
        Cart cart = cartService.getOrCreateCart(userId);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.ARTICLE_CREATED.getCode())
                .message(ResponseStatus.ARTICLE_CREATED.getMessage())
                .data(Map.of("cart", cart))
                .build();
        return httpResponse;
    }
}
