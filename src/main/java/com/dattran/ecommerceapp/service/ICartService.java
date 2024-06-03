package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.dto.CartDTO;
import com.dattran.ecommerceapp.entity.Cart;

public interface ICartService {
    CartDTO getOrCreateCart(String userId);
    CartDTO addItemToCart(String cartId, String productId, Integer quantity, String flavorName);
    CartDTO removeItemFromCart(String cartId, String productId);
}
