package com.springboot.peanut.controller;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.springboot.peanut.data.dto.fcm.FcmSendDto;
import com.springboot.peanut.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationTestController {

    private final NotificationService notificationService;

    @GetMapping("/test/send-notification")
    public ResponseEntity<?> sendTestNotification(
            @RequestParam String token,
            @RequestParam String title,
            @RequestParam String body) {
        try {
            FcmSendDto fcmSendDto = new FcmSendDto(token, title, body);
            notificationService.sendAllNotification(fcmSendDto.getToken(), fcmSendDto.getTitle(), fcmSendDto.getBody());
            return ResponseEntity.ok("알림이 성공적으로 전송되었습니다.");
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.status(500).body("알림 전송 실패: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("알림 전송 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}