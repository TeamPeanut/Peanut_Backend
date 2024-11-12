package com.springboot.peanut.controller;

import com.springboot.peanut.data.dto.fcm.FcmSendDto;
import com.springboot.peanut.service.fcm.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/send")
    public ResponseEntity<?> pushMessage(@RequestBody @Validated FcmSendDto fcmSendDto) throws Exception {
        log.debug("[+] 푸시 메시지를 전송합니다. ");
        HashMap<String, String> map = new HashMap<>();
        ResponseEntity<?> result = fcmService.sendMessageTo(fcmSendDto);
        map.put("result", String.valueOf(result));

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
