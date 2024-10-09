package com.springboot.peanut.controller;

import com.springboot.peanut.data.dto.food.FoodAllDetailDto;
import com.springboot.peanut.data.dto.mainPage.GuardianMainPageGetAdditionalInfoDto;
import com.springboot.peanut.data.dto.mainPage.MedicineInsulinStatusRequestDto;
import com.springboot.peanut.data.dto.mainPage.PatientMainPageGetAdditionalInfoDto;
import com.springboot.peanut.data.dto.mainPage.MainPageGetUserDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.service.MainPage.PatientMainPageService;
import com.springboot.peanut.service.MainPage.GuardianMainService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/main-api")
@RequiredArgsConstructor
public class MainPageController {

    private final PatientMainPageService patientMainPageService;
    private final GuardianMainService GuardianMainService;

    @GetMapping("/get-user")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<MainPageGetUserDto> getUserInfoMainPage(HttpServletRequest request){
        MainPageGetUserDto mainPageGetUserDto = patientMainPageService.getUserInfoMainPage(request);
        return ResponseEntity.status(HttpStatus.OK).body(mainPageGetUserDto);
    }

    @GetMapping("/get-add-info")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<PatientMainPageGetAdditionalInfoDto> getAdditionalInfoMainPage(@RequestParam("date")@DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date ,HttpServletRequest request){
        PatientMainPageGetAdditionalInfoDto patientMainPageGetAdditionalInfoDto = patientMainPageService.getAdditionalInfoMainPage(request,date);
        return ResponseEntity.status(HttpStatus.OK).body(patientMainPageGetAdditionalInfoDto);
    }
     @PostMapping("/get-add-info/save/status")
     @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
      public ResponseEntity<ResultDto> saveMedicineInsulinStatus(HttpServletRequest request, LocalDate date, MedicineInsulinStatusRequestDto medicineInsulinStatusRequestDto){
        ResultDto resultDto = patientMainPageService.saveMedicineInsulinStatus(request,date,medicineInsulinStatusRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
      }


    @GetMapping("/get-all-food")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FoodAllDetailDto> getFoodAllDetail(@RequestParam("date")@DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date,HttpServletRequest request) {
        FoodAllDetailDto foodAllDetailDto = patientMainPageService.getFoodAllDetail(date,request);
        return ResponseEntity.status(HttpStatus.OK).body(foodAllDetailDto);
    }

    @GetMapping("/get-time-food")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FoodAllDetailDto> getFoodDetailByEatTime(@RequestParam("date")@DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam String eatTime, HttpServletRequest request) {
        FoodAllDetailDto foodAllDetailDto = patientMainPageService.getFoodDetailByEatTime(date,eatTime,request);
        return ResponseEntity.status(HttpStatus.OK).body(foodAllDetailDto);
    }

    @GetMapping("/patient/get-user")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<MainPageGetUserDto> getPatientUserInfoMainPage(HttpServletRequest request){
        MainPageGetUserDto mainPageGetUserDto = GuardianMainService.getPatientUserInfoMainPage(request);
        return ResponseEntity.status(HttpStatus.OK).body(mainPageGetUserDto);
    }

    @GetMapping("/patient/get-add-info")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<GuardianMainPageGetAdditionalInfoDto> getPatientAdditionalInfoMainPage(@RequestParam("date")@DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date, HttpServletRequest request){
        GuardianMainPageGetAdditionalInfoDto guardianMainPageGetAdditionalInfoDto = GuardianMainService.getPatientAdditionalInfoMainPage(request,date);
        return ResponseEntity.status(HttpStatus.OK).body(guardianMainPageGetAdditionalInfoDto);
    }

    @GetMapping("/patient/get-all-food")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FoodAllDetailDto> getPatientFoodAllDetail(@RequestParam("date")@DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date,HttpServletRequest request) {
        FoodAllDetailDto foodAllDetailDto = GuardianMainService.getPatientFoodAllDetail(date,request);
        return ResponseEntity.status(HttpStatus.OK).body(foodAllDetailDto);
    }

    @GetMapping("/patient/get-time-food")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FoodAllDetailDto> getFoodPatientDetailByEatTime(@RequestParam("date")@DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam String eatTime, HttpServletRequest request) {
        FoodAllDetailDto foodAllDetailDto = GuardianMainService.getPatientFoodDetailByEatTime(date,eatTime,request);
        return ResponseEntity.status(HttpStatus.OK).body(foodAllDetailDto);
    }
}




