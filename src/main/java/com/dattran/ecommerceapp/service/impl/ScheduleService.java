package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.entity.Notification;
import com.dattran.ecommerceapp.repository.NotificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduleService {
    NotificationRepository notificationRepository;
    @Scheduled(cron = "0 0 0 * * ?") // Run every day at midnight
    @Transactional
    public void deleteOldNotifications() {
        LocalDateTime fourteenDaysAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        List<Notification> notificationsToDelete = notificationRepository.findByCreatedAtBefore(fourteenDaysAgo);
        if (!notificationsToDelete.isEmpty()) {
            notificationRepository.deleteAll(notificationsToDelete);
            System.out.println(notificationsToDelete.size() + " notifications deleted.");
        }
    }
}
