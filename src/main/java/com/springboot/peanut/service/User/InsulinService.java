package com.springboot.peanut.service.User;

import com.springboot.peanut.data.dto.Insulin.InsulinRecordResponseDto;
import com.springboot.peanut.data.dto.Insulin.InsulinRequestDto;
import com.springboot.peanut.data.dto.Insulin.InsulinReportStatus;
import com.springboot.peanut.data.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface InsulinService {
    ResultDto saveInsulinInfo(InsulinRequestDto insulinRequestDto, HttpServletRequest request);
    InsulinReportStatus getInsulinInfoList(int year, int month, HttpServletRequest request);
    List<InsulinRecordResponseDto> getInsulinInfoList(HttpServletRequest request);

    }
