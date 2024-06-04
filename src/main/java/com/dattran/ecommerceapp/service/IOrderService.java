package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.dto.OrderDTO;
import com.dattran.ecommerceapp.dto.response.OrderDetailResponse;
import com.dattran.ecommerceapp.entity.Order;
import com.dattran.ecommerceapp.entity.OrderDetail;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO);
    List<OrderDetailResponse> getOrderDetailByUserId(String userId);
}
