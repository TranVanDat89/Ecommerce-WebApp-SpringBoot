package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.dto.OrderDTO;
import com.dattran.ecommerceapp.entity.Order;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO);
}
