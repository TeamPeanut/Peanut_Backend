package com.springboot.peanut.service.Impl;

import com.springboot.peanut.dao.BloodSugarDao;
import com.springboot.peanut.dto.bloodSugar.BloodSugarRequestDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.entity.BloodSugar;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.service.BloodSugarService;
import com.springboot.peanut.service.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class BloodSugarServiceImpl implements BloodSugarService {
    private final BloodSugarDao bloodSugarDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;
    @Override
    public ResultDto saveBloodSugar(BloodSugarRequestDto bloodSugarRequestDto, HttpServletRequest request) {
        User user = jwtAuthenticationService.authenticationToken(request);
        ResultDto resultDto = new ResultDto();

        BloodSugar bloodSugar = BloodSugar.createBloodSugar(bloodSugarRequestDto, user);
        bloodSugarDao.saveBloodSugar(bloodSugar);
        resultStatusService.setSuccess(resultDto);

        return resultDto;
    }

}
