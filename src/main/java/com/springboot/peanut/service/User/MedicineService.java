package com.springboot.peanut.service.User;

import com.springboot.peanut.data.dto.medicine.MedicineRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;

public interface MedicineService {
    ResultDto saveMedicineInfo (MedicineRequestDto medicineRequestDto, HttpServletRequest httpServletRequest);
}
