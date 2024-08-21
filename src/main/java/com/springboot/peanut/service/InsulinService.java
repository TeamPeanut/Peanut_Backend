package com.springboot.peanut.service;

import com.springboot.peanut.dto.Insulin.InsulinRequestDto;
import com.springboot.peanut.dto.Insulin.InsulinResponseDto;
import com.springboot.peanut.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;

public interface InsulinService {
    InsulinResponseDto saveInsulinInfo(InsulinRequestDto insulinRequestDto, HttpServletRequest request);
}
