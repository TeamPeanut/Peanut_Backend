package com.springboot.peanut.service;

import com.springboot.peanut.dto.bloodSugar.BloodSugarRequestDto;
import com.springboot.peanut.dto.bloodSugar.BloodSugarResponseDto;
import com.springboot.peanut.dto.signDto.ResultDto;


import javax.servlet.http.HttpServletRequest;

public interface BloodSugarService {

    ResultDto saveBloodSugar(BloodSugarRequestDto bloodSugarRequestDto, HttpServletRequest request);



}
