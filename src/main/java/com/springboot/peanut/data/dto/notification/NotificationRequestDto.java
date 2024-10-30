package com.springboot.peanut.data.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

@Getter
@AllArgsConstructor
public class NotificationRequestDto {
    private String title;
    private String body;
    private String fcmToken;

}
