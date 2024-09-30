package com.springboot.peanut.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UserDetailsService userDetailsService;

    private final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${springboot.jwt.secret}")  // 시크릿 키
    private String secretKey = "asdqwe12321s@!#"; // 임시 시크릿 키

    private final long tokenValidTime = 1000 * 60 * 60;

    // 시크릿 키 초기화
    @PostConstruct
    protected void init() {
        logger.info(" [init] 시크릿 키 초기화 시작 ");
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        logger.info("[init] 시크릿 키 초기화 완료 : {} ", secretKey);
    }

    // jwt 토큰으로 인증 정보 조회
    public Authentication getAuthentication(String token) {
        logger.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        logger.info("[getAuthenticaiton] 토큰 인증 정보 조회 완료, UserDetails userName", userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // jwt토큰 생성
    public String createToken(String email, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        logger.info("[createToken] : {} ", token);
        return token;
    }

    // jwt 토큰에서 회원 구별 조회
    public String getUsername(String token) {
        logger.info("[getUsername] 회원 구별 조회 시작");
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // 토큰 본문에서 Subject -> 회원 구별
    }

    // HTTP 요청에서 토큰 추출 (헤더 또는 쿼리 파라미터에서)
    public String resolveToken(HttpServletRequest request) {
        logger.info("[resolveToken] 요청 처리 중...");

        // 1. 쿼리 파라미터에서 토큰을 먼저 추출
        String token = request.getParameter("token");
        logger.info("[resolveToken] token: {}", token);

        // 2. 쿼리 파라미터에 토큰이 없을 경우, 헤더에서 추출
        if (token == null) {
            token = request.getHeader("X-AUTH-TOKEN");
        }

        logger.info("[resolveToken] 토큰 추출 완료: {}", token);
        return token;
    }

    // jwt 토큰의 유효성 검사
    public boolean validToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}