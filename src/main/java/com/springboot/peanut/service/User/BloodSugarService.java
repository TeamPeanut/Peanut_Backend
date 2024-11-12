package com.springboot.peanut.service.User;

import com.springboot.peanut.data.dto.bloodSugar.BloodSugarRequestDto;
import com.springboot.peanut.data.dto.bloodSugar.DailyBloodSugarStatus;
import com.springboot.peanut.data.dto.bloodSugar.MonthlyBloodSugarStatus;
import com.springboot.peanut.data.dto.signDto.ResultDto;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BloodSugarService {

    ResultDto saveBloodSugar(BloodSugarRequestDto bloodSugarRequestDto, HttpServletRequest request);
     MonthlyBloodSugarStatus getMonthlyBloodSugarStatus(int year, int month, HttpServletRequest request);
     MonthlyBloodSugarStatus getGuardianMonthlyBloodSugarStatus(int year, int month, HttpServletRequest request);



    }
