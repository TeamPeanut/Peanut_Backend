package com.springboot.peanut.controller;

import com.springboot.peanut.data.dto.signDto.AdditionalInfoDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.dto.signDto.SignInResultDto;
import com.springboot.peanut.service.User.AuthService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect.url}")
    private String kakaoRedirectUrl;

    @GetMapping("/kakao/get-url")
    public Map<String,String> getKakaoUrl(){
        Map<String, String> response = new HashMap<>();
        String url =  String.format(
                "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s",
                kakaoClientId,kakaoRedirectUrl
        );
        response.put("kakaoURL", url);
        return  response;
    }

    @GetMapping("/kakao/callback")
    @ResponseBody
    public ResponseEntity<?> getKakaoAuthorizeCode(@RequestParam("code") String authorizeCode) {
        log.info("[kakao-login] Received authorizeCode: {}", authorizeCode);

        // 인가 코드를 클라이언트에 반환
        Map<String, String> response = new HashMap<>();
        response.put("AccessToken", authorizeCode);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/kakao/signin")
    public ResponseEntity<?> kakao_SignIn(@RequestParam String accessToken){
        log.info("[kakao-login] accessToken {}", accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(authService.getKakaoUserInfo(accessToken));
    }


    @PutMapping("/kakao/add-info")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> kakao_additionalInfo(AdditionalInfoDto additionalInfoDto , HttpServletRequest request){
        ResultDto resultDto = authService.kakao_additionalInfo(additionalInfoDto,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }


}
