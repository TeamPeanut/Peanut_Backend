package com.springboot.peanut.data.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private String status;

    private LocalDateTime create_At;

    @ManyToOne
    @JoinColumn(name = "patient_id",nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "guardian_id",nullable = false)
    private User guardian;

    public static Notification createNotification(User patient, User guardian, String message, String status){
        Notification notification = new Notification();
        notification.message = message;
        notification.status = status;
        notification.patient = patient;
        notification.guardian = guardian;
        notification.create_At = LocalDateTime.now();
        return notification;
    }


}
