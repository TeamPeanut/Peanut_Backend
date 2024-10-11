package com.springboot.peanut.data.repository;

import com.springboot.peanut.data.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
