package com.springboot.peanut.controller;

import com.springboot.peanut.dto.signDto.AdditionalInfoDto;
import com.springboot.peanut.dto.signDto.SignInResultDto;
import com.springboot.peanut.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> getKakaoAuthorizeCode(@RequestParam("code") String authorizeCode){
        log.info("[kakao-login] authorizeCode {}", authorizeCode);
        return authService.getKakaoUserInfo(authorizeCode);
    }

    @PostMapping("/kakao/signin")
    public SignInResultDto SignIn(String accessToken , @RequestBody AdditionalInfoDto additionalInfoDto){
        log.info("[kakao-login] accessToken {}", accessToken);
        return authService.SignIn(accessToken,additionalInfoDto);
    }


}
