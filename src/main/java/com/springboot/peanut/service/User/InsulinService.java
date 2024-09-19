package com.springboot.peanut.service.User;

import com.springboot.peanut.dto.Insulin.InsulinRequestDto;
import com.springboot.peanut.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;

public interface InsulinService {
    ResultDto saveInsulinInfo(InsulinRequestDto insulinRequestDto, HttpServletRequest request);
}
