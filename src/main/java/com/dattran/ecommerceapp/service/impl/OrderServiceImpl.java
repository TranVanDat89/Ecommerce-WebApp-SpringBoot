package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.dto.CartItemDTO;
import com.dattran.ecommerceapp.dto.OrderDTO;
import com.dattran.ecommerceapp.dto.response.DetailResponse;
import com.dattran.ecommerceapp.dto.response.OrderDetailResponse;
import com.dattran.ecommerceapp.entity.Order;
import com.dattran.ecommerceapp.entity.OrderDetail;
import com.dattran.ecommerceapp.entity.Product;
import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.enumeration.OrderStatus;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.mapper.EntityMapper;
import com.dattran.ecommerceapp.repository.OrderDetailRepository;
import com.dattran.ecommerceapp.repository.OrderRepository;
import com.dattran.ecommerceapp.repository.ProductRepository;
import com.dattran.ecommerceapp.repository.UserRepository;
import com.dattran.ecommerceapp.service.IOrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements IOrderService {
    UserRepository userRepository;
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    EntityMapper entityMapper;
    ProductRepository productRepository;
    @Transactional
    @Override
    public Order createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()->new AppException(ResponseStatus.USER_NOT_FOUND));
        Order order = entityMapper.toOder(orderDTO);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING.getName());
        order.setActive(true);
        order.setTrackingNumber(generateTrackingNumber(10));
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            Product product = productRepository.findById(cartItemDTO.getProduct().getId())
                    .orElseThrow(() -> new AppException(ResponseStatus.PRODUCT_NOT_FOUND));
            orderDetail.setProduct(product);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setNumberOfProducts(cartItemDTO.getQuantity());
            orderDetail.setTotalMoney(orderDetail.getPrice()*orderDetail.getNumberOfProducts());
            orderDetail.setFlavorName(cartItemDTO.getFlavorName());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        return orderRepository.save(order);
    }

    @Override
    public OrderDetailResponse getOrderDetailByUserId(String userId) {
        Order order = orderRepository.findByUserId(userId)
                .orElseThrow(()->new AppException(ResponseStatus.ORDER_NOT_FOUND));
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId())
                .orElseThrow(()->new AppException(ResponseStatus.ORDER_DETAIL_NOT_FOUND));
        List<DetailResponse> detailResponses = new ArrayList<>();
        orderDetails.forEach(orderDetail -> {
            DetailResponse detailResponse = DetailResponse.builder()
                    .productName(orderDetail.getProduct().getName())
                    .price(orderDetail.getPrice())
                    .productId(orderDetail.getProduct().getId())
                    .totalMoney(orderDetail.getTotalMoney())
                    .numberOfProducts(orderDetail.getNumberOfProducts())
                    .productThumbnail(orderDetail.getProduct().getThumbnail())
                    .build();
            detailResponses.add(detailResponse);
        });
        OrderDetailResponse orderDetailResponse = OrderDetailResponse.builder()
                .order(order)
                .detailResponses(detailResponses)
                .build();
        return orderDetailResponse;
    }
    private String generateTrackingNumber(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}
