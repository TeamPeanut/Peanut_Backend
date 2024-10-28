package com.springboot.peanut.service.notification;

import javax.servlet.http.HttpServletRequest;

public interface NotificationService {
    void sendNotification(Long userId, String title, String body,HttpServletRequest request) throws Exception;
    void sendAllNotification(String token, String title, String body) throws Exception;

    }
