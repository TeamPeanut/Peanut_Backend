package com.springboot.peanut.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Configuration
public class FcmConfig {

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @Bean
    public void initializeFirebaseApp() throws IOException {
        // ClassPathResource를 사용하여 리소스 폴더 내 파일 접근
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new FileInputStream(firebaseConfigPath));
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(googleCredentials)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            log.info("FirebaseApp initialized");
        }
    }
}

