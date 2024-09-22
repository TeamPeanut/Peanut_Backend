package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.data.dao.InsulinDao;
import com.springboot.peanut.data.dto.Insulin.InsulinRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.User.InsulinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsulinServiceImpl implements InsulinService {

    private final InsulinDao insulinDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;

    @Override
    public ResultDto saveInsulinInfo(InsulinRequestDto insulinRequestDto, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);

        ResultDto resultDto = new ResultDto();


        if (user != null){
            Insulin insulin = Insulin.createInsulin(insulinRequestDto,user.get());

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
