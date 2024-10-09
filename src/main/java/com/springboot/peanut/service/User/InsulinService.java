package com.springboot.peanut.service.User;

import com.springboot.peanut.data.dto.Insulin.InsulinRecordResponseDto;
import com.springboot.peanut.data.dto.Insulin.InsulinRequestDto;
import com.springboot.peanut.data.dto.medicine.MedicineRecordResponseDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface InsulinService {
    ResultDto saveInsulinInfo(InsulinRequestDto insulinRequestDto, HttpServletRequest request);
    List<InsulinRecordResponseDto> getInsulinInfoList(HttpServletRequest request);

    }
