package com.springboot.peanut.data.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationDto {
    private String sender;
    private String recipient;
    private String message;
    private LocalDateTime create_At;
}

