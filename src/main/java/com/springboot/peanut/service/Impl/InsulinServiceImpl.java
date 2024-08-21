package com.springboot.peanut.service.Impl;

import com.springboot.peanut.dao.InsulinDao;
import com.springboot.peanut.dao.IntakeDao;
import com.springboot.peanut.dao.MedicineDao;
import com.springboot.peanut.dto.CommonResponse;
import com.springboot.peanut.dto.Insulin.InsulinRequestDto;
import com.springboot.peanut.dto.Insulin.InsulinResponseDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.entity.Insulin;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.jwt.JwtProvider;
import com.springboot.peanut.repository.UserRepository;
import com.springboot.peanut.service.InsulinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsulinServiceImpl implements InsulinService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final InsulinDao insulinDao;

    @Override
    public InsulinResponseDto saveInsulinInfo(InsulinRequestDto insulinRequestDto, HttpServletRequest request) {
        String info = jwtProvider.getUsername(request.getHeader("X-AUTH-TOKEN"));
        User user = userRepository.getByEmail(info);
        log.info("[user] : {}" ,user);

        Insulin insulin = new Insulin();
        ResultDto resultDto = new ResultDto();


        if (user != null){
            insulin.setProductName(insulinRequestDto.getProductName());
            insulin.setAdministrationTime(insulinRequestDto.getAdministrationTime());
            insulin.setDosage(insulinRequestDto.getDosage());
            insulin.setAlam(insulinRequestDto.isAlam());
            insulin.setCreate_At(LocalDate.now());

            insulinDao.saveInsulin(insulin);

            InsulinResponseDto insulinResponseDto = new InsulinResponseDto();
            insulinResponseDto.setId(insulin.getId());
            insulinResponseDto.setProductName(insulin.getProductName());
            insulinResponseDto.setAdministrationTime(insulin.getAdministrationTime());
            insulinResponseDto.setDosage(insulin.getDosage());
            insulinResponseDto.setAlam(insulin.isAlam());


            return insulinResponseDto;
        }else {
            throw  new IllegalArgumentException();
        }

    }
    private void setSuccess(ResultDto resultDto) {
        resultDto.setSuccess(true);
        resultDto.setCode(CommonResponse.SUCCESS.getCode());
        resultDto.setMsg(CommonResponse.SUCCESS.getMsg());

    }

    private void setFail(ResultDto resultDto) {
        resultDto.setSuccess(false);
        resultDto.setCode(CommonResponse.Fail.getCode());
        resultDto.setMsg(CommonResponse.Fail.getMsg());
    }
}
