package com.springboot.peanut.service.User;

import com.springboot.peanut.dto.signDto.AdditionalInfoDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.dto.signDto.SignInResultDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    ResponseEntity<?> getKakaoUserInfo(String authorizeCode);
    SignInResultDto kakao_SignIn(String authorizeCode);
    ResultDto kakao_additionalInfo(AdditionalInfoDto additionalInfoDto, HttpServletRequest request);
}
