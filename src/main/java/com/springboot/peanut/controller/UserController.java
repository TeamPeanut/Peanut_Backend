package com.springboot.peanut.controller;

import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.dto.user.UserUpdateRequestDto;
import com.springboot.peanut.service.User.UserService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/update")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    ResponseEntity<ResultDto>updateUserInfo( UserUpdateRequestDto requestDto, @RequestPart("image") MultipartFile image, HttpServletRequest request) throws IOException{
        ResultDto resultDto = userService.updateAdditionalUserInfo(requestDto,image,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @PostMapping("/connect/send-code")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    ResponseEntity<Map<String, String>> sendInviteCode(String email, HttpServletRequest request) throws Exception{
        Map<String, String> map = userService.sendInviteCode(email,request);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
    @PostMapping("/connect/patient-guardian")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    ResponseEntity<ResultDto> confirmGuardianRelation(String confirmationCode, HttpServletRequest request){
        ResultDto resultDto = userService.confirmGuardianRelation(confirmationCode,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }


    }
