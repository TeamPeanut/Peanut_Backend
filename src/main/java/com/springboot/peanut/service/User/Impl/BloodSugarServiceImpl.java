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
        // 포맷터 목록을 설정: "a h시" (분이 없는 경우)와 "a h시 m분" (분이 있는 경우)
        DateTimeFormatter formatterWithMinutes = DateTimeFormatter.ofPattern("a h시 m분", Locale.KOREAN);
        DateTimeFormatter formatterWithoutMinutes = DateTimeFormatter.ofPattern("a h시", Locale.KOREAN);

        LocalTime time;

        try {
            // 먼저 "a h시 m분" 포맷으로 파싱 시도
            time = LocalTime.parse(measuredTime, formatterWithMinutes);
        } catch (Exception e) {
            // "a h시 m분" 파싱 실패 시 "a h시" 포맷으로 파싱 시도
            time = LocalTime.parse(measuredTime, formatterWithoutMinutes);
        }

        // 현재 날짜에 파싱된 시간 결합
        LocalDateTime dateTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), time);

        return dateTime;
    }
}
