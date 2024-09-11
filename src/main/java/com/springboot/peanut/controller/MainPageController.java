package com.springboot.peanut.controller;

import com.springboot.peanut.dto.food.FoodAllDetailDto;
import com.springboot.peanut.dto.mainPage.MainPageGetAdditionalInfoDto;
import com.springboot.peanut.dto.mainPage.MainPageGetUserDto;
import com.springboot.peanut.service.MainPageService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

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

    @GetMapping("/get-add-info")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<MainPageGetAdditionalInfoDto> getAdditionalInfoMainPage(@RequestParam("date")@DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date, HttpServletRequest request){
        MainPageGetAdditionalInfoDto mainPageGetAdditionalInfoDto = mainPageService.getAdditionalInfoMainPage(request,date);
        return ResponseEntity.status(HttpStatus.OK).body(mainPageGetAdditionalInfoDto);
    }

    @GetMapping("/get-all-food")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FoodAllDetailDto> getFoodAllDetail(@RequestParam("date")@DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date,HttpServletRequest request) {
        FoodAllDetailDto foodAllDetailDto = mainPageService.getFoodAllDetail(date,request);
        return ResponseEntity.status(HttpStatus.OK).body(foodAllDetailDto);
    }

    @GetMapping("/get-time-food")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FoodAllDetailDto> getFoodDetailByEatTime(@RequestParam("date")@DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam String eatTime, HttpServletRequest request) {
        FoodAllDetailDto foodAllDetailDto = mainPageService.getFoodDetailByEatTime(date,eatTime,request);
        return ResponseEntity.status(HttpStatus.OK).body(foodAllDetailDto);
    }
}



