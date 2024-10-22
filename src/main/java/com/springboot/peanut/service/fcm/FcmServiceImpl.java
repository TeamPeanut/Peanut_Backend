package com.springboot.peanut.service.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.springboot.peanut.data.dto.fcm.FcmMessageDto;
import com.springboot.peanut.data.dto.fcm.FcmSendDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {
    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @Value("${firebase.api.url}")
    private String firebaseApiUrl;

    @Override
    public int sendMessageTo(FcmSendDto fcmSendDto) throws IOException {
        String message = makeMessage(fcmSendDto);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity entity = new HttpEntity(message, httpHeaders);
        ResponseEntity response = restTemplate.exchange(firebaseApiUrl, HttpMethod.POST,entity,String.class);
        log.info(response.getBody().toString());


        return response.getStatusCode() == HttpStatus.OK ? 1 : 0;

    }

    // Firebase Admin SDK 비공개 키 참조 -> Bearer 토큰 발급
    private String getAccessToken() throws IOException{
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("<https://www.googleapis.com/auth/cloud-platform>"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    //FCM 전송 정보를 기반으로 메시지 구성
    private String makeMessage(FcmSendDto fcmSendDto)throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
                .message(FcmMessageDto.Message.builder()
                        .token(fcmSendDto.getToken())
                        .notification(FcmMessageDto.Notification.builder()
                                .title(fcmSendDto.getTitle())
                                .body(fcmSendDto.getBody())
                                .image(null)
                                .build())
                        .build()).validateOnly(false).build();
        return objectMapper.writeValueAsString(fcmMessageDto);
    }

}
