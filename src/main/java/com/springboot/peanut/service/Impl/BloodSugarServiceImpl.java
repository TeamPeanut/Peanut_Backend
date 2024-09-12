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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

        LocalDateTime measurementTime = StringToDateTime(bloodSugarRequestDto.getMeasurementTime());

        BloodSugar bloodSugar = BloodSugar.createBloodSugar(bloodSugarRequestDto, user);
        bloodSugar.setMeasurementTime(measurementTime);

        bloodSugarDao.saveBloodSugar(bloodSugar);
        resultStatusService.setSuccess(resultDto);

        return resultDto;
    }

    public LocalDateTime StringToDateTime(String measuredTime) {
        // "오전 07시" 같은 문자열을 파싱할 포맷터 생성 (a: 오전/오후, h: 시)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h시", Locale.KOREAN);

        // 시간만 파싱
        LocalTime time = LocalTime.parse(measuredTime, formatter);

        // 현재 날짜에 파싱된 시간 결합
        LocalDateTime dateTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), time);

        return dateTime;
    }
}
