package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.dto.CartDTO;
import com.dattran.ecommerceapp.dto.request.CartUpdateRequest;
import com.dattran.ecommerceapp.entity.Cart;

import java.util.List;

public interface ICartService {
    CartDTO getOrCreateCart(String userId);
    CartDTO addItemToCart(String cartId, String productId, Integer quantity, String flavorName);
    CartDTO removeItemFromCart(String cartId, String productId);
    CartDTO updateCart(CartDTO cartDTO, String userId);
}
