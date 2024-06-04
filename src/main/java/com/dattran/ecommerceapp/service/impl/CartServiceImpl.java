package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.dto.CartDTO;
import com.dattran.ecommerceapp.dto.CartItemDTO;
import com.dattran.ecommerceapp.dto.request.CartRequest;
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

import java.util.List;
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
    public CartDTO getOrCreateCart(String userId) {
        Optional<Cart> optionalCart;
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ResponseStatus.USER_NOT_FOUND));
        optionalCart = cartRepository.findByUserId(userId);
        Cart cart =  optionalCart.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
        return convertToCartDTO(cart);
    }

    @Override
    public CartDTO addItemToCart(String cartId, String productId, Integer quantity, String flavorName) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new AppException(ResponseStatus.CART_NOT_FOUND));
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        Product product = productService.getProductById(productId);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setFlavorName(flavorName);
        cart.getCartItems().add(cartItem);
        cart.setTotalPrice(cart.getCartItems().stream().map(CartItem::getProduct).mapToDouble((p) -> p.getPrice() * quantity).sum());
        Cart cartSaved =  cartRepository.save(cart);
        return convertToCartDTO(cartSaved);
    }

    @Override
    public CartDTO removeItemFromCart(String cartId, String productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new AppException(ResponseStatus.CART_NOT_FOUND));
        cart.getCartItems().removeIf(cartItem -> cartItem.getProduct().getId().equals(productId));
        cart.setTotalPrice(getTotalPrice(cart.getCartItems()));
        Cart cartSaved = cartRepository.save(cart);
        return convertToCartDTO(cartSaved);
    }

    @Override
    public CartDTO updateCart(List<CartRequest> cartUpdate, String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ResponseStatus.CART_NOT_FOUND));
        for (CartItem cartItem : cart.getCartItems()) {
            Optional<CartRequest> optionalCartRequest = cartUpdate.stream().filter(cartRequest -> cartRequest.getProductId().equals(cartItem.getProduct().getId())).findFirst();
            if (optionalCartRequest.isPresent()) {
                CartRequest cartRequest = optionalCartRequest.get();
                cartItem.setQuantity(cartRequest.getQuantity());
                cartItem.setFlavorName(cartRequest.getFlavorName());
            }
        }
//        for (CartRequest cartRequest : cartUpdate) {
//            Optional<CartItem> optionalCartItem = cart.getCartItems().stream().filter(cartItem -> cartItem.getProduct().getId().equals(cartRequest.getProductId())).findFirst();
//            if (optionalCartItem.isPresent()) {
//                CartItem cartItem = optionalCartItem.get();
//                cartItem.setQuantity(cartRequest.getQuantity());
//                cartItem.setFlavorName(cartRequest.getFlavorName());
//                cart.getCartItems().add(cartItem);
//            }
//        }
        cart.setTotalPrice(getTotalPrice(cart.getCartItems()));
        Cart cartSaved = cartRepository.save(cart);
        return convertToCartDTO(cartSaved);
    }

    private Double getTotalPrice(List<CartItem> cartItems) {
        double totalPrice = 0.0;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            totalPrice += product.getPrice() * quantity;
        }
        return totalPrice;
    }
    private CartDTO convertToCartDTO(Cart cart) {
        List<CartItemDTO> cartItemDTOS = cart.getCartItems().stream().map(cartItem -> {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setProduct(cartItem.getProduct());
            cartItemDTO.setQuantity(cartItem.getQuantity());
            cartItemDTO.setFlavorName(cartItem.getFlavorName());
            return cartItemDTO;
        }).toList();
        CartDTO cartDTO = CartDTO.builder()
                .id(cart.getId())
                .totalPrice(cart.getTotalPrice())
                .userId(cart.getUser().getId())
                .cartItems(cartItemDTOS)
                .build();
        return cartDTO;
    }
}
