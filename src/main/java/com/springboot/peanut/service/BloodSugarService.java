package com.springboot.peanut.service;

import com.springboot.peanut.dto.bloodSugar.BloodSugarRequestDto;
import com.springboot.peanut.dto.bloodSugar.BloodSugarResponseDto;


import javax.servlet.http.HttpServletRequest;

public interface BloodSugarService {

    BloodSugarResponseDto saveBloodSugar(BloodSugarRequestDto bloodSugarRequestDto, HttpServletRequest request);



}
