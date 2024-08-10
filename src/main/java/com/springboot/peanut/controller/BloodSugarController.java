package com.springboot.peanut.controller;

import com.amazonaws.Response;
import com.springboot.peanut.dto.BloodSugarDto.CurrentResponseDto;
import com.springboot.peanut.dto.BloodSugarDto.FastingResponseDto;
import com.springboot.peanut.service.BloodSugarService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/blood-sugar")
@RequiredArgsConstructor
public class BloodSugarController {

    private final BloodSugarService bloodSugarService;


    @PostMapping("/fasting")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FastingResponseDto>saveFastingBloodSugar(int fasting_blood_sugar, HttpServletRequest request){
        FastingResponseDto fastingResponseDto = bloodSugarService.saveFastingBloodSugar(fasting_blood_sugar,request);
        return ResponseEntity.status(HttpStatus.OK).body(fastingResponseDto);
    }

    @PostMapping("/current")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<CurrentResponseDto>saveCurrentBloodSugar(int current_blood_sugar, HttpServletRequest request){
        CurrentResponseDto currentResponseDto = bloodSugarService.saveCurrentBloodSugar(current_blood_sugar,request);
        return ResponseEntity.status(HttpStatus.OK).body(currentResponseDto);
    }
}
