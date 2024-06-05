package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.entity.Notification;
import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.repository.NotificationRepository;
import com.dattran.ecommerceapp.repository.UserRepository;
import com.dattran.ecommerceapp.service.INotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements INotificationService {
    NotificationRepository notificationRepository;
    UserRepository userRepository;
    @Override
    public List<Notification> getNotificationByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ResponseStatus.USER_NOT_FOUND));
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return !notifications.isEmpty() ? notifications : List.of();
    }
}
