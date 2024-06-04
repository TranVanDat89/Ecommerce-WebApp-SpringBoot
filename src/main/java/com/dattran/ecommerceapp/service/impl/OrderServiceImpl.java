package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.dto.CartItemDTO;
import com.dattran.ecommerceapp.dto.OrderDTO;
import com.dattran.ecommerceapp.dto.request.CartRequest;
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
        Order order = new Order();
        order.setFullName(orderDTO.getFullName());
        order.setPhoneNumber(orderDTO.getPhoneNumber());
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING.getName());
        order.setActive(true);
        order.setTotalMoney(orderDTO.getTotalMoney());
        order.setShippingAddress(orderDTO.getAddress());
        order.setShippingMethod(orderDTO.getShippingMethod());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setTrackingNumber(generateTrackingNumber(10));
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartRequest cartRequest : orderDTO.getCartItems()) {
            Product product = productRepository.findById(cartRequest.getProductId())
                    .orElseThrow(()->new AppException(ResponseStatus.PRODUCT_NOT_FOUND));
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(cartRequest.getQuantity());
            orderDetail.setPrice(product.getPrice());
            orderDetail.setTotalMoney(product.getPrice() * cartRequest.getQuantity());
            orderDetail.setOrder(order);
            orderDetail.setFlavorName(cartRequest.getFlavorName());
            orderDetails.add(orderDetail);
        }
//        orderDetailRepository.saveAll(orderDetails);
        order.setOrderDetails(orderDetails);
        return orderRepository.save(order);
    }

    @Override
    public List<OrderDetailResponse> getOrderDetailByUserId(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
        orders.forEach(order -> {
            List<OrderDetail> orderDetails = order.getOrderDetails();
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
            orderDetailResponses.add(orderDetailResponse);
        });
        return orderDetailResponses;
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
