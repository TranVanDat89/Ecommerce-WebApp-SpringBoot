package com.dattran.ecommerceapp.controller;

import com.dattran.ecommerceapp.dto.CartDTO;
import com.dattran.ecommerceapp.dto.request.CartUpdateRequest;
import com.dattran.ecommerceapp.dto.response.HttpResponse;
import com.dattran.ecommerceapp.entity.Cart;
import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.service.ICartService;
import com.dattran.ecommerceapp.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/carts")
public class CartController {
    ICartService cartService;
    SecurityUtil securityUtil;
    @PostMapping("/add-to-cart")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public HttpResponse addToCart(@RequestParam String productId, @RequestParam int quantity,
                                  @RequestParam String flavorName,
                                  HttpServletRequest httpServletRequest) {
        User loggedUser = securityUtil.getLoggedInUserInfor();
        String userId = null;
        if (loggedUser != null) {
            userId = loggedUser.getId();
        }
        CartDTO cart = cartService.getOrCreateCart(userId);
        cart = cartService.addItemToCart(cart.getId(), productId, quantity, flavorName);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.ADD_TO_CART_SUCCESS.getCode())
                .message(ResponseStatus.ADD_TO_CART_SUCCESS.getMessage())
                .data(Map.of("cart", cart))
                .build();
        return httpResponse;
    }
    @PostMapping("/update-cart")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public HttpResponse updateCart(@RequestBody CartDTO cart, HttpServletRequest httpServletRequest) {
        User loggedUser = securityUtil.getLoggedInUserInfor();
        String userId = null;
        if (loggedUser != null) {
            userId = loggedUser.getId();
        }
        CartDTO cartChanged = cartService.updateCart(cart, userId);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.UPDATE_CART_SUCCESS.getCode())
                .message(ResponseStatus.UPDATE_CART_SUCCESS.getMessage())
                .data(Map.of("cart", cartChanged))
                .build();
        return httpResponse;
    }

    @GetMapping("/my-cart")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public HttpResponse getCart(HttpServletRequest httpServletRequest) {
        User loggedUser = securityUtil.getLoggedInUserInfor();
        String userId = null;
        if (loggedUser != null) {
            userId = loggedUser.getId();
        }
        CartDTO cart = cartService.getOrCreateCart(userId);
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
    @GetMapping("/delete-item")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public HttpResponse deleteCartItem(@RequestParam String productId, HttpServletRequest httpServletRequest) {
        User loggedUser = securityUtil.getLoggedInUserInfor();
        String userId = null;
        if (loggedUser != null) {
            userId = loggedUser.getId();
        }
        CartDTO cart = cartService.getOrCreateCart(userId);
        CartDTO cartDTO = cartService.removeItemFromCart(cart.getId(), productId);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.REMOVE_FROM_CART_SUCCESS.getCode())
                .message(ResponseStatus.REMOVE_FROM_CART_SUCCESS.getMessage())
                .data(Map.of("cart", cartDTO))
                .build();
        return httpResponse;
    }
}
