package com.springboot.peanut.service.fcm;

import com.springboot.peanut.data.dto.fcm.FcmSendDto;
import org.springframework.http.ResponseEntity;

public interface FcmService {
    ResponseEntity<?> sendMessageTo  (FcmSendDto fcmSendDto) throws Exception;
}
