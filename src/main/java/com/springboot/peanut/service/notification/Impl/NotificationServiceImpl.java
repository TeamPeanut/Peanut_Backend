package com.springboot.peanut.service.notification.Impl;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.springboot.peanut.data.dao.NotificationDao;
import com.springboot.peanut.data.dao.UserDao;
import com.springboot.peanut.data.dto.fcm.FcmSendDto;
import com.springboot.peanut.data.entity.Notification;
import com.springboot.peanut.data.entity.PatientGuardian;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.PatientGuardianRepository;
import com.springboot.peanut.data.repository.UserRepository;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.fcm.FcmService;
import com.springboot.peanut.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JwtAuthenticationService jwtAuthenticationService;
    private final FcmService fcmService;

    @Override
    public void sendNotification(Long userId, String title, String body, HttpServletRequest request) throws Exception {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        String fcmToken = user.get().getFcmToken();
        if (fcmToken != null && !fcmToken.isEmpty()) {
            FcmSendDto fcmSendDto = new FcmSendDto(fcmToken, title, body);
            try {
                fcmService.sendMessageTo(fcmSendDto);
            } catch (FirebaseMessagingException e) {
                // 로그 기록 또는 에러 처리
                log.error("FCM 알림 전송 실패: " + e.getMessage());
            }
        } else {
            log.warn("해당 사용자의 FCM 토큰이 없습니다.");
        }
    }

    @Override
    public void sendAllNotification(String token, String title, String body) throws Exception {
        FcmSendDto fcmSendDto = new FcmSendDto(token,title,body);
        fcmService.sendMessageTo(fcmSendDto);
    }
}