package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.dto.OrderDTO;
import com.dattran.ecommerceapp.dto.request.CartRequest;
import com.dattran.ecommerceapp.dto.request.OrderStatusRequest;
import com.dattran.ecommerceapp.dto.response.DetailResponse;
import com.dattran.ecommerceapp.dto.response.OrderDetailResponse;
import com.dattran.ecommerceapp.entity.*;
import com.dattran.ecommerceapp.enumeration.OrderStatus;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.mapper.EntityMapper;
import com.dattran.ecommerceapp.repository.*;
import com.dattran.ecommerceapp.service.IOrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements IOrderService {
    UserRepository userRepository;
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    EntityMapper entityMapper;
    ProductRepository productRepository;
    NotificationRepository notificationRepository;
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
        order.setIsCommented(false);
        order.setTotalMoney(orderDTO.getTotalMoney());
        order.setShippingAddress(orderDTO.getAddress());
        order.setShippingMethod(orderDTO.getShippingMethod());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setTrackingNumber(generateTrackingNumber(10));
        StringBuilder message = new StringBuilder();
        message.append("Đơn hàng của bạn gồm: ");
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartRequest cartRequest : orderDTO.getCartItems()) {
            Product product = productRepository.findById(cartRequest.getProductId())
                    .orElseThrow(()->new AppException(ResponseStatus.PRODUCT_NOT_FOUND));
            message.append(product.getName()).append(", ");
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
        message.append("với mã đơn hàng ").append(order.getId())
                .append(", tracking number: ").append(order.getTrackingNumber()).append(".");
        Notification notification = Notification.builder()
                .user(user)
                .message(message.toString())
                .build();
        notificationRepository.save(notification);
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
    @Transactional
    @Override
    public Order updateStatusOrder(OrderStatusRequest orderStatusRequest) {
        Order order = orderRepository.findById(orderStatusRequest.getOrderId())
                .orElseThrow(()->new AppException(ResponseStatus.ORDER_NOT_FOUND));
        User user = userRepository.findById(order.getUser().getId())
                        .orElseThrow(()->new AppException(ResponseStatus.USER_NOT_FOUND));
        User delivery = userRepository.findByRoleId("6b32380e-7041-41ee-aa00-bd7523e9fd0b").orElseThrow(()->new AppException(ResponseStatus.USER_NOT_FOUND));
        order.setStatus(orderStatusRequest.getStatus());
        Notification notification = Notification.builder()
                .user(user)
                .build();
        switch (OrderStatus.valueOf(orderStatusRequest.getStatus().toUpperCase())) {
            case SUCCESS:
                order.setShippingDate(LocalDate.from(LocalDateTime.now()));
                   List<OrderDetail> orderDetails = order.getOrderDetails();
                   orderDetails.forEach(orderDetail -> {
                       Product product = orderDetail.getProduct();
                       product.setSolved(product.getSolved()+orderDetail.getNumberOfProducts());
                       product.setQuantity(product.getQuantity()-orderDetail.getNumberOfProducts());
                       productRepository.save(product);
                   });
                   notification.setMessage(String.format("Đơn hàng %s đã giao thành công.", order.getId()));
                   break;

            case DELIVERING:
                Notification notiForDelivering = Notification.builder()
                        .user(delivery)
                        .message(String.format("Bạn có đơn hàng %s.", order.getId()))
                        .build();
                notificationRepository.save(notiForDelivering);
                notification.setMessage(String.format("Đơn hàng %s đang trên đường giao đến bạn. Vui lòng chú ý điện thoại.", order.getId()));
                break;

            case CANCELLED:
                order.setNote(orderStatusRequest.getNote());
                notification.setMessage(String.format("Đơn hàng %s đã bị hủy với lí do %s.", order.getId(), order.getNote()));
                break;
        }
        notificationRepository.save(notification);
        return orderRepository.save(order);
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
