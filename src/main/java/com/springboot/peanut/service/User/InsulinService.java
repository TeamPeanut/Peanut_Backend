package com.springboot.peanut.service.User;

import com.springboot.peanut.data.dto.Insulin.InsulinRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;

public interface InsulinService {
    ResultDto saveInsulinInfo(InsulinRequestDto insulinRequestDto, HttpServletRequest request);
}
