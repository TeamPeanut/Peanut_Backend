package com.springboot.peanut.service.User;

import com.springboot.peanut.data.dto.medicine.MedicineRecordResponseDto;
import com.springboot.peanut.data.dto.medicine.MedicineRecordStatus;
import com.springboot.peanut.data.dto.medicine.MedicineRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MedicineService {
    ResultDto saveMedicineInfo (MedicineRequestDto medicineRequestDto, HttpServletRequest httpServletRequest);
    MedicineRecordStatus getMedicineInfoList (int year, int month , HttpServletRequest request);
}
