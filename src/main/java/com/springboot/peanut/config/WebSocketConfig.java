package com.springboot.peanut.config;

import com.springboot.peanut.data.repository.UserRepository;
import com.springboot.peanut.handshake.HttpHandShakeInterceptors;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .addInterceptors(new HttpHandShakeInterceptors(jwtProvider,userRepository ))
                .setAllowedOriginPatterns("http://localhost:8080")  // 특정 도메인만 허용
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // topic - 여러구독자 동시에, queue - 1대1 한 사용자에게만 알림
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

}
