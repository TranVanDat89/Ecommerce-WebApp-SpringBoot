package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.dto.OrderDTO;
import com.dattran.ecommerceapp.dto.request.OrderStatusRequest;
import com.dattran.ecommerceapp.dto.response.OrderDetailResponse;
import com.dattran.ecommerceapp.entity.Order;
import com.dattran.ecommerceapp.entity.OrderDetail;

import java.util.List;
import java.util.Map;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO);
    List<OrderDetailResponse> getOrderDetailByUserId(String userId);
    Order updateStatusOrder(OrderStatusRequest orderStatusRequest);
    Double calculateOutcomeOfSuccessfulOrders(int year);
//    Long countTotalOrder();
    Long countOrdersByStatusAndYear(String status, int year);
    Map<String, ?> countOrdersByYear(int year);
    Map<String, Double> getTotalMoneyByStatusAndYearGroupedByMonth(String status, int year);
    Map<String, ?> calculateOutcomeOrders(int year);
}
