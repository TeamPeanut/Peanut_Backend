package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.data.dao.*;
import com.springboot.peanut.data.dto.medicine.MedicineRecordResponseDto;
import com.springboot.peanut.data.dto.medicine.MedicineReportResponseDto;
import com.springboot.peanut.data.dto.medicine.MedicineReportStatus;
import com.springboot.peanut.data.dto.medicine.MedicineRequestDto;
import com.springboot.peanut.data.dto.notification.NotificationRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.*;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.User.MedicineService;
import com.springboot.peanut.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {


    private final MedicineDao medicineDao;
    private final MedicineRecordDao medicineRecordDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;
    private final IntakeDao intakeDao;
    private final UserDao userDao;
    private final NotificationService notificationService;
    private final NotificationDao notificationDao;

    @Override
    public ResultDto saveMedicineInfo(MedicineRequestDto medicineRequestDto, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);

        ResultDto resultDto = new ResultDto();


        if (user.isPresent()) {
            // Medicine 객체 생성
            Medicine medicine = Medicine.createMedicine(medicineRequestDto.getMedicineName(),user.get());
            medicineDao.saveMedicineInfo(medicine);
            log.info("[medicineInfo] : {}", medicine);

            // Intake 객체 생성 및 Medicine에 추가
            Intake intake = Intake.createIntake(
                    medicineRequestDto.getIntakeDays(),
                    medicineRequestDto.getIntakeTime(),
                    user.get(),
                    medicine
            );
            medicine.addIntake(intake);
            intakeDao.saveIntakeInfo(intake);
            log.info("[intakeInfo] : {}", intake);

            resultDto.setDetailMessage("약 정보 입력 완료!");
            resultStatusService.setSuccess(resultDto);
        }else{
            resultStatusService. setFail(resultDto);
            throw new IllegalArgumentException();
        }

        return resultDto;
    }

    @Override
    public ResultDto stopMedicine(Long medicineId, boolean activeStatus, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        Long userId = user.get().getId();
        ResultDto resultDto = new ResultDto();

        medicineDao.stopMedicine(medicineId, userId, activeStatus);
        resultDto.setDetailMessage("회원님의 복약 정보가 저장되었습니다.");
        resultStatusService.setSuccess(resultDto);

        return resultDto;

    }
    @Override
    public List<MedicineRecordResponseDto> getMedicineInfoList(HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        if (user.isPresent()) {
            List<Medicine> medicine = medicineDao.getMedicineByUserId(user.get().getId());
            List<MedicineRecordResponseDto> medicineRecordResponseDtoList = new ArrayList<>();

            // 각 약에 대해 반복
            for (Medicine m : medicine) {
                List<Intake> intakeList = m.getIntakes();
                if(m.isActiveStatus()){
                // Intake 리스트에서 intakeDays를 추출하여 하나의 리스트로 병합
                List<String> allIntakeDays = intakeList.stream()
                        .flatMap(intake -> intake.getIntakeDays().stream())
                        .collect(Collectors.toList());

                // Intake 리스트에서 intakeTime을 추출하여 하나의 리스트로 병합
                List<String> allIntakeTimes = intakeList.stream()
                        .flatMap(intake -> intake.getIntakeTime().stream())
                        .collect(Collectors.toList());

                // 각 약에 대한 DTO 생성
                MedicineRecordResponseDto medicineRecordResponseDto = new MedicineRecordResponseDto(
                        m.getId(),
                        m.getMedicineName(),
                        "복약 중",
                        allIntakeDays,
                        allIntakeTimes
                );

                // DTO를 리스트에 추가
                medicineRecordResponseDtoList.add(medicineRecordResponseDto);
                }else{
                    MedicineRecordResponseDto medicineRecordResponseDto = new MedicineRecordResponseDto(
                            m.getId(),
                            m.getMedicineName(),
                            "복약 중단",
                            null,
                            null
                    );

                    // DTO를 리스트에 추가
                    medicineRecordResponseDtoList.add(medicineRecordResponseDto);

                }
            }
            return medicineRecordResponseDtoList;
        }else{
            throw new IllegalArgumentException("복용 약이 없습니다.");
        }


    }

    @Override
    public MedicineReportStatus getMedicineInfoList(int year, int month, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        if (user.isPresent()) {
            List<MedicineRecord> medicineRecordList = medicineRecordDao.findMedicineByYearAndMonth(user.get().getId(), year, month);
            List<MedicineReportResponseDto> medicineReportResponseDtoList = new ArrayList<>();
            int cnt = 0;
            // 각 약에 대해 반복
            for (MedicineRecord m : medicineRecordList) {
                LocalDate date = m.getRecordDate();
                cnt++;
                String recordStatus = cntStatus(cnt);

                // 각 약에 대한 DTO 생성
                MedicineReportResponseDto medicineReportResponseDto = new MedicineReportResponseDto(
                        date,
                        recordStatus
                );
                medicineReportResponseDtoList.add(medicineReportResponseDto);
            }
            String monthlyReport = monthlyReport(cnt);
            MedicineReportStatus medicineReportStatus = new MedicineReportStatus(
                    medicineReportResponseDtoList,
                    monthlyReport
            );
         return medicineReportStatus;
        }else{
            throw new IllegalArgumentException("복용 약이 없습니다.");
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

    @Scheduled(cron = "0 31 13 * * ?")  // "점심 후" 시간대 알림
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
            List<String> administrationTimes = intakeDao.findIntakeTime(user.getId());

            // 사용자가 설정한 투약 시간대가 현재 스케줄링된 시간대와 일치하는지 확인
            if (administrationTimes.contains(administrationTime)) {
                String fcmToken = user.getFcmToken();
                String title = "복약 알림";
                String body = user.getUserName() + "님! " + administrationTime + " 복약 시간입니다. 적시에 복약해 주세요.";
                log.info("[fcmToken] : {}", fcmToken);
                log.info("[userName] : {}", user.getUserName());
                log.info("[body] : {}", body);
                saveNotification(title,body,fcmToken,user);
                if (fcmToken != null && !fcmToken.isEmpty()) {
                    notificationService.sendAllNotification(fcmToken,title, body);
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
            return "총 복약량의" +cnt+"일을 복약했어요! 건강을 위해서라도 더 신경써서 복약하는게 어떨까요?";
        }else if(10<=cnt&&cnt<15){
            return "총 복약량의" +cnt+"일을 복약했어요! 건강을 생각하며 더 복약하면 좋을 것 같아요! ";
        }else if(15<=cnt&&cnt<20){
            return "총 복약량의" +cnt+"일을 복약했어요! 이번달 절반 이상 복약했어요! 조금만 더 노력하여 건강을 챙겨보아요!";
        }else if(20<=cnt&&cnt<31) {
            return "총 복약량의" + cnt + "일을 복약했어요! 매우 잘했어요! 앞으로 더욱 건강한 생활이 가능할 거예요!";
        }else {
            return null;
        }
    }

    public void saveNotification(String title, String body,String fcmToken, User user) {
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(
                title,
                body,
                fcmToken
        );
        Notification notification = Notification.saveNotificationInfo(notificationRequestDto,user);

        notificationDao.save(notification);
    }

}
