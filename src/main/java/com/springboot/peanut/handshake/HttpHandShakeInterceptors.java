package com.springboot.peanut.handshake;

import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.UserRepository;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class HttpHandShakeInterceptors implements HandshakeInterceptor {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

            // 토큰을 쿼리 파라미터에서 가져옴
            String token = jwtProvider.resolveToken(servletRequest);

            if (token == null || token.isEmpty()) {
                log.error("JWT 토큰이 전달되지 않았습니다. WebSocket 연결 거부");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            // 토큰이 유효한지 확인
            if (!jwtProvider.validToken(token)) {
                log.error("유효하지 않은 JWT 토큰입니다. WebSocket 연결 거부");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            // 토큰으로부터 사용자 정보 추출
            String email = jwtProvider.getUsername(token);
            Optional<User> user = userRepository.findByEmail(email);

            if (user.isPresent()) {
                attributes.put("user", user.get());
                return true; // WebSocket 연결 허용
            } else {
                log.error("사용자 정보를 찾을 수 없습니다. WebSocket 연결 거부");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
        }

        // 요청이 ServletServerHttpRequest가 아닌 경우 연결 거부
        return false;
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 핸드셰이크 후 로직 필요 시 작성
    }


}