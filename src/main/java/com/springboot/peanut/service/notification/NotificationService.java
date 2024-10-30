package com.springboot.peanut.service.notification;

import javax.servlet.http.HttpServletRequest;

public interface NotificationService {
    void sendNotification(String token, String title, String body)  throws Exception;
    void sendAllNotification(String token, String title, String body) throws Exception;

    }
