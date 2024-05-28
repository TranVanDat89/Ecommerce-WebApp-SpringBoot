package com.dattran.ecommerceapp.controller;

import com.dattran.ecommerceapp.dto.OrderDTO;
import com.dattran.ecommerceapp.dto.ProductDTO;
import com.dattran.ecommerceapp.dto.response.HttpResponse;
import com.dattran.ecommerceapp.dto.response.OrderDetailResponse;
import com.dattran.ecommerceapp.entity.Order;
import com.dattran.ecommerceapp.entity.OrderDetail;
import com.dattran.ecommerceapp.entity.Product;
import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.service.IOrderService;
import com.dattran.ecommerceapp.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    SecurityUtil securityUtil;
    IOrderService orderService;

    @PostMapping("/create-order")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public HttpResponse createOrder(@RequestBody @Valid OrderDTO orderDTO, HttpServletRequest httpServletRequest) {
        User loggedInUser = securityUtil.getLoggedInUserInfor();
        if (orderDTO.getUserId() == null) {
            orderDTO.setUserId(loggedInUser.getId());
        }
        Order order = orderService.createOrder(orderDTO);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.CREATED)
                .statusCode(ResponseStatus.ORDER_CREATED.getCode())
                .message(ResponseStatus.ORDER_CREATED.getMessage())
                .data(Map.of("order", order))
                .build();
        return httpResponse;
    }
    @GetMapping("/order-detail/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public HttpResponse getOrderDetail(@PathVariable("userId") String userId, HttpServletRequest httpServletRequest) {
        OrderDetailResponse orderDetails = orderService.getOrderDetailByUserId(userId);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.CREATED)
                .statusCode(ResponseStatus.GET_ORDER_DETAIL_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_ORDER_DETAIL_SUCCESSFULLY.getMessage())
                .data(Map.of("orderDetails", orderDetails))
                .build();
        return httpResponse;
    }
}
