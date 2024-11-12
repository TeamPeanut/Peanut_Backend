package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.dto.notification.NotificationListResponseDto;
import com.springboot.peanut.data.entity.Notification;

import java.util.List;

public interface NotificationDao {
    void save(Notification notification);
    List<NotificationListResponseDto> getNotificationListByUserId(Long userId);
}
