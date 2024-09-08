package com.springboot.peanut.controller;

import com.springboot.peanut.dto.mainPage.MainPageGetUserDto;
import com.springboot.peanut.service.MainPageService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/main-api")
@RequiredArgsConstructor
public class MainPageController {

    private final MainPageService mainPageService;

    @GetMapping("/get-user")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<MainPageGetUserDto> getUserInfoMainPage(HttpServletRequest request){
        MainPageGetUserDto mainPageGetUserDto = mainPageService.getUserInfoMainPage(request);
        return ResponseEntity.status(HttpStatus.OK).body(mainPageGetUserDto);
    }
}
