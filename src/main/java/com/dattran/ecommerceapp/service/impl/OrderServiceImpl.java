package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.dto.CartItemDTO;
import com.dattran.ecommerceapp.dto.OrderDTO;
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
    @Transactional
    @Override
    public Order createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()->new AppException(ResponseStatus.USER_NOT_FOUND));
        Order order = entityMapper.toOder(orderDTO);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING.getName());
        order.setActive(true);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            Product product = productRepository.findById(cartItemDTO.getProductId())
                    .orElseThrow(() -> new AppException(ResponseStatus.PRODUCT_NOT_FOUND));
            orderDetail.setProduct(product);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setNumberOfProducts(cartItemDTO.getQuantity());
            orderDetail.setTotalMoney(orderDetail.getPrice()*orderDetail.getNumberOfProducts());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        return orderRepository.save(order);
    }
}
