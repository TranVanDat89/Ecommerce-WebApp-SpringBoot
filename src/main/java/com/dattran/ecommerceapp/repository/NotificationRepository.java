package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    Notification findByUserId(String userId);
}
