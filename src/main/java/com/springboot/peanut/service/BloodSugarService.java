package com.springboot.peanut.service;

import com.springboot.peanut.dto.BloodSugarDto.BloodSugarRequestDto;
import com.springboot.peanut.dto.BloodSugarDto.BloodSugarResponseDto;


import javax.servlet.http.HttpServletRequest;

public interface BloodSugarService {

    BloodSugarResponseDto saveBloodSugar(BloodSugarRequestDto bloodSugarRequestDto, HttpServletRequest request);



}
