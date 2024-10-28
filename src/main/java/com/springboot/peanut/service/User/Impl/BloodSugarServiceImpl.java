package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.data.dao.BloodSugarDao;
import com.springboot.peanut.data.dto.bloodSugar.BloodSugarRequestDto;
import com.springboot.peanut.data.dto.bloodSugar.DailyBloodSugarStatus;
import com.springboot.peanut.data.dto.bloodSugar.MonthlyBloodSugarStatus;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.BloodSugar;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.UserRepository;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.User.BloodSugarService;
import com.springboot.peanut.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BloodSugarServiceImpl implements BloodSugarService {
    private final BloodSugarDao bloodSugarDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
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
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendSpandrelNotification() throws Exception {
        List<User> users = userRepository.findAll();  // 모든 사용자 조회 또는 특정 조건에 맞는 사용자 조회

        for (User user : users) {
            String fcmToken = user.getFcmToken();
            String userName = user.getUsername();
            String body = userName + "님을 정확하게 진단 해드리고 싶어요. 공복 혈당을 재고 기록헤주세요!";

            if (fcmToken != null && !fcmToken.isEmpty()) {
                notificationService.sendAllNotification(fcmToken, "혈당 알림", body);
            } else {
                log.warn("해당 사용자의 FCM 토큰이 없습니다: " + user.getUsername());
            }
        }
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void sendBeforeMealNotification() throws Exception {
        List<User> users = userRepository.findAll();  // 모든 사용자 조회 또는 특정 조건에 맞는 사용자 조회

        for (User user : users) {
            String fcmToken = user.getFcmToken();
            String userName = user.getUsername();
            String body = userName + "님을 정확하게 진단 하기 위해서 오늘의 식전 혈당을 재고 기록헤주세요!";

            if (fcmToken != null && !fcmToken.isEmpty()) {
                notificationService.sendAllNotification(fcmToken, "혈당 알림", body);
            } else {
                log.warn("해당 사용자의 FCM 토큰이 없습니다: " + user.getUsername());
            }
        }
    }
    @Scheduled(cron = "0 0 18 * * ?")
    public void sendAfterMealNotification() throws Exception {
        List<User> users = userRepository.findAll();  // 모든 사용자 조회 또는 특정 조건에 맞는 사용자 조회

        for (User user : users) {
            String fcmToken = user.getFcmToken();
            String userName = user.getUsername();
            String body = userName + "님을 정확하게 진단 하기 위해서 오늘의 식후 혈당을 재고 기록헤주세요!";

            if (fcmToken != null && !fcmToken.isEmpty()) {
                notificationService.sendAllNotification(fcmToken, "혈당 알림", body);
            } else {
                log.warn("해당 사용자의 FCM 토큰이 없습니다: " + user.getUsername());
            }
        }
    }
    @Scheduled(cron = "0 0 18 * * ?")
    public void sendBeforeSleepNotification() throws Exception {
        List<User> users = userRepository.findAll();  // 모든 사용자 조회 또는 특정 조건에 맞는 사용자 조회

        for (User user : users) {
            String fcmToken = user.getFcmToken();
            String userName = user.getUsername();
            String body = userName + "님! 오늘 하루 혈당 기록을 하셨나요? 자기 전 혈당을 재고 기록헤주세요!";

            if (fcmToken != null && !fcmToken.isEmpty()) {
                notificationService.sendAllNotification(fcmToken, "혈당 알림", body);
            } else {
                log.warn("해당 사용자의 FCM 토큰이 없습니다: " + user.getUsername());
            }
        }
    }
}
