package com.springboot.peanut.controller;

import com.springboot.peanut.dto.bloodSugar.BloodSugarRequestDto;
import com.springboot.peanut.dto.bloodSugar.BloodSugarResponseDto;
import com.springboot.peanut.dto.signDto.ResultDto;
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


    @PostMapping("/save")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> saveBloodSugar(BloodSugarRequestDto bloodSugarRequestDto, HttpServletRequest request){
        ResultDto resultDto = bloodSugarService.saveBloodSugar(bloodSugarRequestDto,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }
}
