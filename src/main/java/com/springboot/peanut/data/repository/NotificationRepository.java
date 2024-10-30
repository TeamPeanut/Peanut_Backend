package com.springboot.peanut.data.repository;

import com.springboot.peanut.data.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findNotificationByUserId(Long userId);
}
