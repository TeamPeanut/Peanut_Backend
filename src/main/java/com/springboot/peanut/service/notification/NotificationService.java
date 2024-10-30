package com.springboot.peanut.service.notification;

import com.springboot.peanut.data.dto.notification.NotificationListResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface NotificationService {
    void sendNotification(String token, String title, String body)  throws Exception;
    void sendAllNotification(String token, String title, String body) throws Exception;
    List<NotificationListResponseDto> getNotificationList(HttpServletRequest request) throws Exception;
    }
