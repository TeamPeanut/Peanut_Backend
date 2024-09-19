package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.dao.InsulinDao;
import com.springboot.peanut.dto.Insulin.InsulinRequestDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.entity.Insulin;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.service.Jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.User.InsulinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsulinServiceImpl implements InsulinService {

    private final InsulinDao insulinDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;

    @Override
    public ResultDto saveInsulinInfo(InsulinRequestDto insulinRequestDto, HttpServletRequest request) {
        User user = jwtAuthenticationService.authenticationToken(request);

        ResultDto resultDto = new ResultDto();


        if (user != null){
            Insulin insulin = Insulin.createInsulin(insulinRequestDto,user);

            insulinDao.saveInsulin(insulin);
            resultDto.setDetailMessage("회원님의 인슐린 정보가 저장되었습니다.");
            resultStatusService.setSuccess(resultDto);

        }else {
            resultDto.setDetailMessage("인슐린 정보 저장 실패");
            resultStatusService.setFail(resultDto);
            throw  new IllegalArgumentException();
        }
        return resultDto;
    }

}
