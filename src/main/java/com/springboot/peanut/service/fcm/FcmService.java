package com.springboot.peanut.service.fcm;

import com.springboot.peanut.data.dto.fcm.FcmSendDto;

public interface FcmService {
    int sendMessageTo  (FcmSendDto fcmSendDto) throws Exception;
}
