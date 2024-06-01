package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.entity.Cart;

public interface ICartService {
    Cart getOrCreateCart(String userId, String sessionId);
    Cart addItemToCart(String cartId, String productId, Integer quantity, String flavorName);
}
