package com.springboot.peanut.service.Impl;

import com.springboot.peanut.dto.CommonResponse;
import com.springboot.peanut.dto.signDto.ResultDto;
import org.springframework.stereotype.Service;

@Service
public class ResultStatusService {

    public void setSuccess(ResultDto resultDto) {
        resultDto.setSuccess(true);
        resultDto.setCode(CommonResponse.SUCCESS.getCode());
        resultDto.setMsg(CommonResponse.SUCCESS.getMsg());

    }

    public void setFail(ResultDto resultDto) {
        resultDto.setSuccess(false);
        resultDto.setCode(CommonResponse.Fail.getCode());
        resultDto.setMsg(CommonResponse.Fail.getMsg());
    }
}
