package com.springboot.peanut.controller;


import com.springboot.peanut.service.notification.NotificationService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    // 환자가 약을 먹었을 때 보호자에게 알림
    @PostMapping("/api/medication/taken")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public void notifyMedicationTaken(HttpServletRequest request) {
        notificationService.sendMedicationNotification(request,"TAKEN");
    }

    // 환자가 약을 먹지 않았을 때 보호자에게 알림
    @PostMapping("/api/medication/not-taken")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public void notifyMedicationNotTaken(HttpServletRequest request) {
        notificationService.sendMedicationNotification(request, "NOT_TAKEN");
    }

    // 보호자가 환자에게 약 복용을 상기시키는 요청
    @PostMapping("/api/caregiver/remind")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public void remindPatient(HttpServletRequest request) {
        notificationService.remindPatient(request,"SENT");
    }
}