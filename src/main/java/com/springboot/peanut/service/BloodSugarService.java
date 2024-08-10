package com.springboot.peanut.service;

import com.springboot.peanut.dto.BloodSugarDto.CurrentResponseDto;
import com.springboot.peanut.dto.BloodSugarDto.FastingResponseDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface BloodSugarService {

    FastingResponseDto saveFastingBloodSugar(int fasting_blood_sugar, HttpServletRequest request);
    CurrentResponseDto saveCurrentBloodSugar(int current_blood_sugar, HttpServletRequest request);



}
