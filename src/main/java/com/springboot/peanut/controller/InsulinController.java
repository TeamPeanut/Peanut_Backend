package com.springboot.peanut.controller;

import com.springboot.peanut.dto.Insulin.InsulinRequestDto;
import com.springboot.peanut.dto.Insulin.InsulinResponseDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.service.InsulinService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/insulin")
@RequiredArgsConstructor
public class InsulinController {

    private final InsulinService insulinService;

    @PostMapping("/save")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<InsulinResponseDto> saveInsulinInfo(InsulinRequestDto insulinRequestDto, HttpServletRequest request){
        InsulinResponseDto insulinResponseDto = insulinService.saveInsulinInfo(insulinRequestDto,request);
        return ResponseEntity.status(HttpStatus.OK).body(insulinResponseDto);
    }
}
