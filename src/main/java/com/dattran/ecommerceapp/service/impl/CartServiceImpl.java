package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.entity.Cart;
import com.dattran.ecommerceapp.entity.CartItem;
import com.dattran.ecommerceapp.entity.Product;
import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.repository.CartItemRepository;
import com.dattran.ecommerceapp.repository.CartRepository;
import com.dattran.ecommerceapp.repository.UserRepository;
import com.dattran.ecommerceapp.service.ICartService;
import com.dattran.ecommerceapp.service.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements ICartService {
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    UserRepository userRepository;
    IProductService productService;
    @Override
    public Cart getOrCreateCart(String userId) {
        Optional<Cart> optionalCart;
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ResponseStatus.USER_NOT_FOUND));
        optionalCart = cartRepository.findByUserId(userId);
        return optionalCart.orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    @Override
    public Cart addItemToCart(String cartId, String productId, Integer quantity, String flavorName) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new AppException(ResponseStatus.CART_NOT_FOUND));
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        Product product = productService.getProductById(productId);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setFlavorName(flavorName);
        cart.getCartItems().add(cartItem);
//        cart.getItems().add(cartItem);
        return cartRepository.save(cart);
    }
}
