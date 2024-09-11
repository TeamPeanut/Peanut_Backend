package com.springboot.peanut.service;

import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.dto.signDto.SignUpDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface SignService {
    Map<String,String> sendSimpleMessage(String to, HttpServletRequest request) throws Exception;
    Map<String,String> verifyEmail(String confirmationCode, HttpServletRequest request);

    ResultDto SignUp (SignUpDto signUpDto,HttpServletRequest request);


    ResultDto SignIn(String email, String password);
}
