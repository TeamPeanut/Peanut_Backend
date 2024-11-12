package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.data.dao.InsulinDao;
import com.springboot.peanut.data.dao.InsulinRecordDao;
import com.springboot.peanut.data.dao.NotificationDao;
import com.springboot.peanut.data.dao.UserDao;
import com.springboot.peanut.data.dto.Insulin.*;
import com.springboot.peanut.data.dto.notification.NotificationRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.*;
import com.springboot.peanut.data.repository.PatientGuardianRepository;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.User.InsulinService;
import com.springboot.peanut.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsulinServiceImpl implements InsulinService {

    private final InsulinDao insulinDao;
    private final InsulinRecordDao insulinRecordDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;
    private final NotificationDao notificationDao;
    private final NotificationService notificationService;
    private final UserDao userDao;
    private final PatientGuardianRepository patientGuardianRepository;

    @Override
    public ResultDto saveInsulinInfo(InsulinRequestDto insulinRequestDto, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);

        ResultDto resultDto = new ResultDto();

        if (user.isPresent()){
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

    @Override
    public ResultDto stopInsulin(Long insulinId, boolean activeStatus, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        Long userId = user.get().getId();
        ResultDto resultDto = new ResultDto();

        insulinDao.stopInsulin(insulinId, userId, activeStatus);
        resultDto.setDetailMessage("회원님의 인슐린 정보가 저장되었습니다.");
        resultStatusService.setSuccess(resultDto);

        return resultDto;

    }

    @Override
    public List<InsulinRecordResponseDto> getInsulinInfoList(HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        if (user.isPresent()) {
            Insulin insulin = insulinDao.getInsulinByUserId(user.get().getId());
            List<InsulinRecordResponseDto> insulinRecordResponseDtoList = new ArrayList<>();
                if (insulin.isActiveStatus()) {
                List<String> administrationTime = insulin.getAdministrationTime();
                InsulinRecordResponseDto insulinRecordResponseDto = new InsulinRecordResponseDto(
                        insulin.getId(),
                        insulin.getProductName(),
                        "투약 중",
                        insulin.getDosage(),
                        administrationTime
                );
                    insulinRecordResponseDtoList.add(insulinRecordResponseDto);
                }else{
                    InsulinRecordResponseDto insulinRecordResponseDto = new InsulinRecordResponseDto(
                            insulin.getId(),
                            insulin.getProductName(),
                            "투약 중단 상태",
                            null,
                            null
                    );
                    insulinRecordResponseDtoList.add(insulinRecordResponseDto);
                }
            return insulinRecordResponseDtoList;

        }else{
            throw new IllegalArgumentException("투약 인슐린이 없습니다.");
        }

    }
    @Override
    public InsulinReportStatus getInsulinInfoList(int year, int month, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        if (user.isPresent()) {
            // 해당 유저의 년도와 달에 따른 인슐린 기록을 가져옵니다
            List<InsulinRecord> insulinRecordList = insulinRecordDao.findInsulinByYearAndMonth(user.get().getId(), year, month);
            List<InsulinReportResponseDto> insulinReportResponseDtoList = new ArrayList<>();
            int cnt = 0;

            // 각 날짜에 대해 일일 측정 횟수(cnt)를 계산합니다.
            for (InsulinRecord insulinRecord : insulinRecordList) {
                LocalDate date = insulinRecord.getRecordDate();
                cnt++;
                String recordStatus = cntStatus(cnt);

                // 해당 날짜의 응답 DTO 생성
                InsulinReportResponseDto insulinRecordResponseDto = new InsulinReportResponseDto(
                        date,
                        recordStatus
                );
                insulinReportResponseDtoList.add(insulinRecordResponseDto);
            }
            String monthlyReport = monthlyReport(cnt);
            InsulinReportStatus insulinReportStatus = new InsulinReportStatus(
                    insulinReportResponseDtoList,
                    monthlyReport

            );

            return insulinReportStatus;
        } else {
            throw new IllegalArgumentException("투약 인슐린 정보가 없습니다.");
        }
    }

    @Override
    public InsulinReportStatus getGuardianInsulinInfoList(int year, int month, HttpServletRequest request) {
        Optional<User> guardian = jwtAuthenticationService.authenticationToken(request);
        PatientGuardian patientGuardian = patientGuardianRepository.findByGuardianId(guardian.get().getId());

        if (patientGuardian !=null) {
            // 해당 유저의 년도와 달에 따른 인슐린 기록을 가져옵니다
            List<InsulinRecord> insulinRecordList = insulinRecordDao.findInsulinByYearAndMonth(patientGuardian.getId(), year, month);
            List<InsulinReportResponseDto> insulinReportResponseDtoList = new ArrayList<>();
            int cnt = 0;

            // 각 날짜에 대해 일일 측정 횟수(cnt)를 계산합니다.
            for (InsulinRecord insulinRecord : insulinRecordList) {
                LocalDate date = insulinRecord.getRecordDate();
                cnt++;
                String recordStatus = cntStatus(cnt);

                // 해당 날짜의 응답 DTO 생성
                InsulinReportResponseDto insulinRecordResponseDto = new InsulinReportResponseDto(
                        date,
                        recordStatus
                );
                insulinReportResponseDtoList.add(insulinRecordResponseDto);
            }
            String monthlyReport = monthlyReport(cnt);
            InsulinReportStatus insulinReportStatus = new InsulinReportStatus(
                    insulinReportResponseDtoList,
                    monthlyReport

            );

            return insulinReportStatus;
        } else {
            throw new IllegalArgumentException("투약 인슐린 정보가 없습니다.");
        }
    }
    // 각 시간대에 대한 알림 스케줄링 메서드
    @Scheduled(cron = "0 0 8 * * ?")  // "아침 후" 시간대 알림
    public void sendMorningAfterNotification() throws Exception {
        sendScheduledInsulinNotification("아침 후");
    }

    @Scheduled(cron = "0 30 11 * * ?") // "점심 전" 시간대 알림
    public void sendLunchBeforeNotification() throws Exception {
        sendScheduledInsulinNotification("점심 전");
    }

    @Scheduled(cron = "0 11 13 * * ?")  // "점심 후" 시간대 알림
    public void sendLunchAfterNotification() throws Exception {
        sendScheduledInsulinNotification("점심 후");
    }

    @Scheduled(cron = "0 0 17 * * ?")  // "저녁 전" 시간대 알림
    public void sendDinnerBeforeNotification() throws Exception {
        sendScheduledInsulinNotification("저녁 전");
    }

    @Scheduled(cron = "0 0 19 * * ?")  // "저녁 후" 시간대 알림
    public void sendDinnerAfterNotification() throws Exception {
        sendScheduledInsulinNotification("저녁 후");
    }

    @Scheduled(cron = "0 0 22 * * ?")  // "자기 전" 시간대 알림
    public void sendBedtimeNotification() throws Exception {
        sendScheduledInsulinNotification("자기 전");
    }


    public void sendScheduledInsulinNotification(String administrationTime) throws Exception {
        List<User> users = userDao.findAllUser();

        for (User user : users) {
            List<String> administrationTimes = insulinDao.findAdministrationTimeByUserId(user.getId());
            log.info("[user] : {}", user);
            log.info("[administrationTimes] : {}", administrationTimes);
            // 사용자가 설정한 투약 시간대가 현재 스케줄링된 시간대와 일치하는지 확인
            if (administrationTimes.contains(administrationTime)) {
                String fcmToken = user.getFcmToken();
                String title = "인슐린 알림";
                String body = user.getUserName() + "님! " + administrationTime + " 인슐린 투여 시간입니다. 적시에 투약해주세요.";
                log.info("[fcmToken] : {}", fcmToken);
                log.info("[userName] : {}", user.getUserName());
                log.info("[body] : {}", body);
                saveNotification( title, body, fcmToken, user);
                if (fcmToken != null && !fcmToken.isEmpty()) {
                    notificationService.sendAllNotification(fcmToken, "인슐린 알림", body);
                } else {
                    log.warn("해당 사용자의 FCM 토큰이 없습니다: " + user.getUsername());
                }
            }
        }
    }

    // 매일 측정 상태를 반환하는 메서드
    public String cntStatus(int cnt) {
        if (cnt == 0) {
            return "아쉬워요";
        } else if (cnt == 1 || cnt == 2) {
            return "보통이에요";
        } else if (cnt >= 3) {
            return "참 잘했어요";
        } else {
            return null;
        }
    }


    public String monthlyReport(int cnt){

        if(0<=cnt&&cnt<10){
            return "총 투여일의" +cnt+"일을 투여했어요! 건강을 위해서라도 더 신경써서 투약하는게 어떨까요?";
        }else if(10<=cnt&&cnt<15){
            return "총 투여일의" +cnt+"일을 투여했어요! 건강을 생각하며 더 복약하면 좋을 것 같아요! ";
        }else if(15<=cnt&&cnt<20){
            return "총 투여일의" +cnt+"일을 투여했어요! 이번달 절반 이상 투여했어요! 조금만 더 노력하여 건강을 챙겨보아요!";
        }else if(20<=cnt&&cnt<31) {
            return "총 투여일의" + cnt + "일을 투여했어요! 매우 잘했어요! 앞으로 더욱 건강한 생활이 가능할 거예요!";
        }else {
            return null;
        }
    }
    public void saveNotification(String title, String body,String fcnToken, User user) {
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(
                title,
                body,
                fcnToken
        );
        Notification notification = Notification.saveNotificationInfo(notificationRequestDto,user);

        notificationDao.save(notification);
    }
}


