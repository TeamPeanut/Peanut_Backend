package com.springboot.peanut.controller;

import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.dto.signDto.SignUpDto;
import com.springboot.peanut.service.User.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sign")
public class SignController {

    private final SignService signService;

    @PostMapping("/send-mail")
    public ResponseEntity<Map<String, String>> sendSimpleMessage(@RequestParam String email, HttpServletRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(signService.sendSimpleMessage(email,request));
    }
    @PostMapping("/verified")
    public ResponseEntity<?> verifyEmail(@RequestParam String confirmationCode, HttpServletRequest request){
        Map<String,String> verified = signService.verifyEmail(confirmationCode,request);
        return ResponseEntity.status(HttpStatus.OK).body(verified);
    }
    @PostMapping("/sign-up")
    public ResponseEntity<ResultDto> SignUp(SignUpDto signUpDto, HttpServletRequest request){
        ResultDto resultDto = signService.SignUp(signUpDto,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }
    @PostMapping("/sign-in")
    public ResponseEntity<ResultDto> SignIn(String email, String password){
        ResultDto resultDto = signService.SignIn(email,password);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);

    }

}
