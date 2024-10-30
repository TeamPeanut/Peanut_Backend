package com.springboot.peanut.data.entity;

import com.springboot.peanut.data.dto.notification.NotificationRequestDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    private String title;

    private LocalDateTime create_At;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Notification saveNotificationInfo(NotificationRequestDto notificationRequestDto,User user) {
        return Notification.builder()
                .body(notificationRequestDto.getBody())
                .title(notificationRequestDto.getTitle())
                .user(user)
                .create_At(LocalDateTime.now())
                .build();
    }
}
