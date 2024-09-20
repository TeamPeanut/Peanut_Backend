package com.springboot.peanut.controller;

import com.springboot.peanut.data.dto.signDto.AdditionalInfoDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.dto.signDto.SignInResultDto;
import com.springboot.peanut.service.User.AuthService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    public SignInResultDto kakao_SignIn(@RequestParam String accessToken){
        log.info("[kakao-login] accessToken {}", accessToken);
        return authService.kakao_SignIn(accessToken);
    }

    @PutMapping("/kakao/add-info")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> kakao_additionalInfo(AdditionalInfoDto additionalInfoDto , HttpServletRequest request){
        ResultDto resultDto = authService.kakao_additionalInfo(additionalInfoDto,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }


}
