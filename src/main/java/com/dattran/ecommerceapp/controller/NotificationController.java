package com.dattran.ecommerceapp.controller;

import com.dattran.ecommerceapp.dto.response.HttpResponse;
import com.dattran.ecommerceapp.entity.Notification;
import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.service.INotificationService;
import com.dattran.ecommerceapp.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/notifications")
public class NotificationController {
    INotificationService notificationService;
    SecurityUtil securityUtil;
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_DELIVERY')")
    public HttpResponse getNotificationByUserId(HttpServletRequest httpServletRequest) {
        User loggedInUser = securityUtil.getLoggedInUserInfor();
        List<Notification> notifications = notificationService.getNotificationByUserId(loggedInUser.getId());
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_ALL_NOTIFICATION_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_ALL_NOTIFICATION_SUCCESSFULLY.getMessage())
                .data(Map.of("notifications", notifications))
                .build();
        return httpResponse;
    }
}
