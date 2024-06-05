package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.entity.Notification;

import java.util.List;

public interface INotificationService {
    List<Notification> getNotificationByUserId(String userId);
}
