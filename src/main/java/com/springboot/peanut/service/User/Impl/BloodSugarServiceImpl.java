package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.data.dao.BloodSugarDao;
import com.springboot.peanut.data.dto.bloodSugar.BloodSugarRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.BloodSugar;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.User.BloodSugarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BloodSugarServiceImpl implements BloodSugarService {
    private final BloodSugarDao bloodSugarDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;
    @Override
    public ResultDto saveBloodSugar(BloodSugarRequestDto bloodSugarRequestDto, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        ResultDto resultDto = new ResultDto();

        LocalDateTime measurementTime = StringToDateTime(bloodSugarRequestDto.getMeasurementTime());

        BloodSugar bloodSugar = BloodSugar.createBloodSugar(bloodSugarRequestDto, user.get());
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
