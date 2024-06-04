package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.dto.CartDTO;
import com.dattran.ecommerceapp.dto.request.CartRequest;

import java.util.List;

public interface ICartService {
    CartDTO getOrCreateCart(String userId);
    CartDTO addItemToCart(String cartId, String productId, Integer quantity, String flavorName);
    CartDTO removeItemFromCart(String cartId, String productId);
    CartDTO updateCart(List<CartRequest> cartUpdate, String cartId);
}
