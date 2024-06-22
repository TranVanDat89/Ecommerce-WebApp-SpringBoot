package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
    List<Notification> findByCreatedAtBefore(LocalDateTime dateTime);
}
