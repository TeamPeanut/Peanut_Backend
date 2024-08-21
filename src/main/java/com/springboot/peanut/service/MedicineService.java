package com.springboot.peanut.service;

import com.springboot.peanut.dto.medicine.MedicineRequestDto;
import com.springboot.peanut.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;

public interface MedicineService {
    ResultDto saveMedicineInfo (MedicineRequestDto medicineRequestDto, HttpServletRequest httpServletRequest);
}
