package com.springboot.peanut.controller;


import com.springboot.peanut.data.dto.notification.NotificationListResponseDto;
import com.springboot.peanut.service.notification.NotificationService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/get-all")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<NotificationListResponseDto>> getNotificationList(HttpServletRequest request) throws Exception {
        List<NotificationListResponseDto> notificationList = notificationService.getNotificationList(request);
        return ResponseEntity.status(HttpStatus.OK).body(notificationList);
    }


    }