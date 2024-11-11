package com.springboot.peanut.service.fcm.Impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.springboot.peanut.data.dto.fcm.FcmSendDto;
import com.springboot.peanut.service.fcm.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {

    @Override
    public ResponseEntity<?> sendMessageTo(FcmSendDto fcmSendDto) throws FirebaseMessagingException {

        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        // FCM 메시지를 구성
        Notification notification = Notification.builder()
                .setTitle(fcmSendDto.getTitle())
                .setBody(fcmSendDto.getBody())
                .build();

        Message message = Message.builder()
                .setToken(fcmSendDto.getToken())
                .setNotification(notification)
                .build();

        // Firebase 메시지 전송
        firebaseMessaging.send(message);
        return ResponseEntity.ok().build();
    }
}
