package com.springboot.peanut.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Slf4j
@Configuration
public class FcmConfig {
    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @Bean
    public void initializeFirebaseApp() throws IOException {
        // 파일 경로를 직접 확인해보고, null이나 빈 파일이 아닌지 로그를 통해 확인할 수 있습니다.
        ClassPathResource resource = new ClassPathResource(firebaseConfigPath);

        // Firebase JSON 파일이 존재하지 않는 경우
        if (!resource.exists()) {
            throw new IllegalArgumentException("Firebase JSON file not found");
        }

        // Firebase JSON 파일이 비어 있는 경우
        if (resource.contentLength() == 0) {
            throw new IllegalArgumentException("Firebase JSON file is empty");
        }

        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(resource.getInputStream());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(googleCredentials)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            log.info("FirebaseApp initialized");
        }
    }
}