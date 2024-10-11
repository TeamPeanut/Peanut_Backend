package com.springboot.peanut.service.notification;

import javax.servlet.http.HttpServletRequest;

public interface NotificationService {
    void sendMedicationNotification(HttpServletRequest request , String status);

    void remindPatient(HttpServletRequest request, String status);


    }
