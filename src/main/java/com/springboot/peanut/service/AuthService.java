package com.springboot.peanut.service;

import com.springboot.peanut.dto.signDto.AdditionalInfoDto;
import com.springboot.peanut.dto.signDto.SignInResultDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> getKakaoUserInfo(String authorizeCode);
    SignInResultDto SignIn(String authorizeCode , AdditionalInfoDto additionalInfoDto);

}
