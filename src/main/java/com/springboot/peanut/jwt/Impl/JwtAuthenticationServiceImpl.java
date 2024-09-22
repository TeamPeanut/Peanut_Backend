package com.springboot.peanut.jwt.Impl;

import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.jwt.JwtProvider;
import com.springboot.peanut.data.repository.UserRepository;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public  Optional<User> authenticationToken(HttpServletRequest request) {
        try {
            // 토큰 추출
            String token = jwtProvider.resolveToken(request);
            if (token == null) {
                throw new IllegalArgumentException("토큰이 요청에 포함되지 않았습니다.");
            }

            // 토큰에서 이메일 추출
            String email = jwtProvider.getUsername(token);
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("토큰에서 이메일을 추출할 수 없습니다.");
            }

            // 이메일을 이용해 유저 찾기
            Optional<User> user = userRepository.findByEmail(email);
            if (user == null) {
                throw new IllegalArgumentException("해당 이메일로 등록된 유저를 찾을 수 없습니다: " + email);
            }

            return user;
        } catch (JwtException e) {
            // JWT 관련 예외 처리
            throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.", e);
        } catch (Exception e) {
            // 기타 예외 처리
            throw new IllegalArgumentException("인증 토큰 처리 중 오류가 발생했습니다.", e);
        }
    }

}
