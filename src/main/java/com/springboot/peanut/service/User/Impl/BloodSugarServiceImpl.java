package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.data.dao.BloodSugarDao;
import com.springboot.peanut.data.dto.bloodSugar.BloodSugarRequestDto;
import com.springboot.peanut.data.dto.bloodSugar.DailyBloodSugarStatus;
import com.springboot.peanut.data.dto.bloodSugar.MonthlyBloodSugarStatus;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.BloodSugar;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.User.BloodSugarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @Override
    public MonthlyBloodSugarStatus getMonthlyBloodSugarStatus(int year, int month, HttpServletRequest request) {
        User user = jwtAuthenticationService.authenticationToken(request).get();

        List<BloodSugar> bloodSugarList = bloodSugarDao.findByUserAndMonth(user.getId(),year,month);

        Map<LocalDate,List<BloodSugar>> dailyRecords = new HashMap<>();
        for(BloodSugar bloodSugar : bloodSugarList) {
            LocalDate date = bloodSugar.getMeasurementTime().toLocalDate();
            dailyRecords.computeIfAbsent(date, k -> new ArrayList<>()).add(bloodSugar);
        }

        double totalAverage = 0.0; // 월 평균 혈당 수치를 저장할 변수
        int totalDays = dailyRecords.size(); // 총 일수

        List<DailyBloodSugarStatus> dailyBloodSugarStatusList  = new ArrayList<>();
        for(Map.Entry<LocalDate,List<BloodSugar>> entry : dailyRecords.entrySet()) {
            LocalDate measurementDate = entry.getKey();
            List<BloodSugar> dailyBloodSugars = entry.getValue();

            double avgBloodSugar = dailyBloodSugars.stream()
                    .mapToDouble(bloodSugar ->Double.parseDouble(bloodSugar.getBloodSugarLevel()))
                    .average()
                    .orElse(0.0);
            String bloodSugarStatus = determineBloodSugarStatus(avgBloodSugar);
            dailyBloodSugarStatusList.add(new DailyBloodSugarStatus(measurementDate,bloodSugarStatus));

            totalAverage += avgBloodSugar;
        }

        double monthlyAvg = totalDays>0 ?totalAverage/totalDays : 0.0;
        String monthlyStatusMessage = generateMonthlyStatusMessage(monthlyAvg);
        String monthlyAvgStatus = determineBloodSugarStatus(monthlyAvg);

        return new MonthlyBloodSugarStatus(monthlyAvg,monthlyAvgStatus, monthlyStatusMessage, dailyBloodSugarStatusList);
    }
    private String determineBloodSugarStatus(double average) {
        if (average < 70) {
            return "저혈당 수치";
        } else if (average <= 130) {
            return "정상 수치";
        } else if(average <= 180) {
            return "고혈당 수치";
        }else {
            return "위험 수치";
        }


    }
    private String generateMonthlyStatusMessage(double average) {
        String status = determineBloodSugarStatus(average);
        switch (status) {
            case "정상 수치":
                return "이번 달은 정상 수치를 잘 유지하고 있네요.";
            case "저혈당 수치":
                return "이번 달은 저혈당 수치를 주의해야 합니다.";
            case "고혈당 수치":
                return "이번 달은 고혈당 수치를 주의해야 합니다.";
            case "위험 수치":
                return "이번 달은 위험 수치에 주의해야 합니다.";
            default:
                return "혈당 수치 정보가 없습니다.";
        }

        }
}
