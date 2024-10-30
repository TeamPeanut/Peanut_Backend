package com.springboot.peanut.data.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationListResponseDto {
    private Long id;
    private String title;
    private String body;
    private LocalDateTime create_At;
}
