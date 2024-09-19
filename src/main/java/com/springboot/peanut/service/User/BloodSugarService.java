package com.springboot.peanut.service.User;

import com.springboot.peanut.dto.bloodSugar.BloodSugarRequestDto;
import com.springboot.peanut.dto.signDto.ResultDto;


import javax.servlet.http.HttpServletRequest;

public interface BloodSugarService {

    ResultDto saveBloodSugar(BloodSugarRequestDto bloodSugarRequestDto, HttpServletRequest request);



}
