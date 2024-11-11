package com.springboot.peanut.service.MainPage.Impl;

import com.springboot.peanut.data.dao.*;
import com.springboot.peanut.data.dto.food.FoodAllDetailDto;
import com.springboot.peanut.data.dto.mainPage.MedicineInsulinStatusRequestDto;
import com.springboot.peanut.data.dto.mainPage.PatientMainPageGetAdditionalInfoDto;
import com.springboot.peanut.data.dto.mainPage.MainPageGetUserDto;
import com.springboot.peanut.data.dto.notification.NotificationRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.*;
import com.springboot.peanut.data.repository.Intake.IntakeRepository;
import com.springboot.peanut.data.repository.PatientGuardianRepository;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.MainPage.MainPageTimeService;
import com.springboot.peanut.service.MainPage.PatientMainPageService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientMainPageServiceImpl implements PatientMainPageService {

    private final BloodSugarDao bloodSugarDao;
    private final NotificationDao notificationDao;
    private final InsulinRecordDao insulinRecordDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final MealDao mealDao;
    private final NotificationService notificationService;
    private final ResultStatusService resultStatusService;
    private final MedicineRecordDao medicineRecordDao;
    private final InsulinDao insulinDao;
    private  final MainPageTimeService mainPageTimeService;
    private final PatientGuardianRepository patientGuardianRepository;

    @Override
    public MainPageGetUserDto getUserInfoMainPage(HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        // 사용자 공복 혈당
        Optional<BloodSugar> fastingBloodSugar = bloodSugarDao.findFastingBloodSugar(user.get().getId());
        String fastingBloodSugarLevel = fastingBloodSugar
                .map(BloodSugar::getBloodSugarLevel)
                .orElse("공복 혈당을 찾을 수 없습니다.");

        // 현재 시간과 가장 가까운 혈당
        Optional<BloodSugar> currentBloodSugarLevel = bloodSugarDao.findClosestBloodSugar(user.get().getId());
        String currentBloodSugar = currentBloodSugarLevel
                .map(BloodSugar::getBloodSugarLevel)
                .orElse("최근에 등록된 혈당을 찾을 수 없습니다.");

        // 생성자로 객체 생성
        return new MainPageGetUserDto(
                user.get().getId(),
                user.get().getUserName(),
                user.get().getProfileUrl(),
                fastingBloodSugarLevel,
                currentBloodSugar
        );
    }

    @Override
    public PatientMainPageGetAdditionalInfoDto getAdditionalInfoMainPage(HttpServletRequest request, LocalDate date) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);

        // 혈당 정보 가져오기
        List<BloodSugar> bloodSugarList = bloodSugarDao.findTodayBloodSugar(user.get().getId(), date);
        bloodSugarList = (bloodSugarList != null) ? bloodSugarList : Collections.emptyList(); // 값이 없으면 빈 리스트로 처리

        // 약 정보 가져오기
        List<Medicine> medicineList = mainPageTimeService.getMedicineListByTime(user.get().getId());
        medicineList = (medicineList != null) ? medicineList : Collections.emptyList(); // 값이 없으면 빈 리스트로 처리

        // 약 복용 기록 정보 가져오기 (Optional 처리)
        Optional<MedicineRecord> medicineRecordInfo = mainPageTimeService.getMedicineRecordByTime(user.get().getId(), date);
        boolean medicineStatus = medicineRecordInfo.map(MedicineRecord::isMedicineStatus).orElse(false);  // 값이 없을 경우 false로 처리

        // 인슐린 정보 가져오기 (빈 Optional을 반환하도록 변경)
        Optional<Insulin> insulin = Optional.ofNullable(insulinDao.getInsulinByUserId(user.get().getId()));
        Optional<InsulinRecord> insulinRecord = mainPageTimeService.getInsulinRecordTime(user.get().getId(), date);
        boolean insulinStatus = insulinRecord.map(InsulinRecord::isInsulinStatus).orElse(false);  // 값이 없을 경우 false로 처리

        // 약 목록에서 첫 번째 약 가져오기
        Medicine medicine = medicineList.isEmpty() ? null : medicineList.get(0);  // 약이 없으면 null 처리
        String medicineName = (medicine != null) ? medicine.getMedicineName() : "약 정보 없음";

        // 복약 시간 추출
        List<String> intakeTimes = medicine != null ? medicine.getIntakes().stream()
                .flatMap(intake -> intake.getIntakeTime().stream())
                .collect(Collectors.toList()) : Collections.emptyList();
        String medicineTime = mainPageTimeService.getIntakeTimeByCurrentTime(intakeTimes);  // 시간대별로 처리

        // 인슐린 관련 처리
        String insulinName = insulin.map(Insulin::getProductName).orElse("인슐린 정보 없음");
        List<String> insulinTimeList = insulin.map(Insulin::getAdministrationTime).orElse(Collections.emptyList());
        String insulinTime = mainPageTimeService.getInsulinTimeByCurrentTime(insulinTimeList);
        String insulinDosage = insulin.map(Insulin::getDosage).orElse("용량 정보 없음");

        // 혈당 기록 리스트 처리
        List<Map<String, Map<Integer, LocalDateTime>>> bloodSugarLevels = bloodSugarList.stream()
                .map(bloodSugar -> {
                    Map<Integer, LocalDateTime> innerMap = new HashMap<>();
                    innerMap.put(Integer.parseInt(bloodSugar.getBloodSugarLevel()), bloodSugar.getMeasurementTime());

                    Map<String, Map<Integer, LocalDateTime>> map = new HashMap<>();
                    map.put(bloodSugar.getMeasurementCondition(), innerMap);
                    return map;
                })
                .collect(Collectors.toList());

        return new PatientMainPageGetAdditionalInfoDto(
                bloodSugarLevels,
                medicineName,
                medicineStatus,
                medicineTime,
                insulinName,
                insulinStatus,
                insulinTime,
                insulinDosage
        );
    }
    @Override
    @Transactional
    public ResultDto saveMedicineInsulinStatus(HttpServletRequest request, LocalDate date, MedicineInsulinStatusRequestDto medicineInsulinStatusRequestDto) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        ResultDto resultDto = new ResultDto();
        Long userId = user.get().getId();

        // patientId로 보호자 정보 조회
        PatientGuardian patientGuardian = patientGuardianRepository.findByPatientId(userId);

        if (patientGuardian != null) {
            User guardian = patientGuardian.getGuardian();

            // 기존 코드 계속 진행
            // Medicine과 Insulin 정보를 가져온다
            List<Medicine> medicineList = mainPageTimeService.getMedicineListByTime(userId);
            Insulin insulin = insulinDao.getInsulinByUserId(userId);

            boolean newMedicineStatus = medicineInsulinStatusRequestDto.isMedicineStatus();
            boolean newInsulinStatus = medicineInsulinStatusRequestDto.isInsulinStatus();

            try {
                // Medicine 상태 업데이트
                if (medicineList != null && !medicineList.isEmpty()) {
                    for (Medicine medicine : medicineList) {
                        Optional<List<MedicineRecord>> existingRecord = medicineRecordDao.findMedicineRecordByUserId(userId, date);

                        if (existingRecord.isPresent() && !existingRecord.get().isEmpty()) {
                            MedicineRecord medicineRecord = existingRecord.get().get(0);
                            medicineRecord.setMedicineStatus(newMedicineStatus);
                            if (medicineRecord.isMedicineStatus()) {
                                String fcmToken = guardian.getFcmToken();
                                String userName = user.get().getUserName();
                                String title = "환자 알림";
                                String body = userName + "님의 환자님께서 알림을 보냈습니다. \n 금일 복약 기록을 완료했습니다.";
                                log.info("[fcmToken] : {}", fcmToken);
                                log.info("[userName] : {}", userName);
                                log.info("[body] : {}", body);
                                notificationService.sendNotification(fcmToken, title, body);
                                saveNotification(title, body, fcmToken, user.get());
                                log.info("[medicine] : {} 기존 레코드 상태 업데이트 완료", medicine.getMedicineName());
                            }
                        } else {
                            MedicineRecord newMedicineRecord = MedicineRecord.createInsulinRecord(date, newMedicineStatus, user.get(), medicine);
                            medicineRecordDao.saveMedicineRecord(newMedicineRecord);
                            log.info("[medicine] : {} 새로운 상태 저장 완료", medicine.getMedicineName());
                        }
                        resultDto.setDetailMessage("약 상태 저장 완료");
                        resultStatusService.setSuccess(resultDto);
                    }
                } else {
                    resultDto.setDetailMessage("저장된 복용 정보가 없습니다.");
                    resultStatusService.setSuccess(resultDto);
                }

                // Insulin 상태 업데이트
                if (insulin != null) {
                    Optional<InsulinRecord> existingInsulinRecord = insulinRecordDao.findInsulinRecordByUserId(userId, date);

                    if (existingInsulinRecord.isPresent()) {
                        InsulinRecord insulinRecord = existingInsulinRecord.get();
                        insulinRecord.setInsulinStatus(newInsulinStatus);
                        String fcmToken = guardian.getFcmToken();
                        String title = "환자 알림";
                        String body = guardian.getUserName() + "님의 환자님께서 알림을 보냈습니다 \n"+user.get().getUserName()+"환자분께서 금일 인슐린 투약 기록을 완료했습니다.";
                        notificationService.sendNotification(fcmToken, title, body);

                        log.info("[insulin] : {} 기존 레코드 상태 업데이트 완료", insulin.getProductName());
                    } else {
                        InsulinRecord newInsulinRecord = InsulinRecord.createInsulinRecord(date, newInsulinStatus, user.get(), insulin);
                        insulinRecordDao.saveInsulinRecord(newInsulinRecord);
                        log.info("[insulin] : {} 새로운 상태 저장 완료", insulin.getProductName());
                    }
                    resultDto.setDetailMessage("인슐린 상태 저장 완료");
                    resultStatusService.setSuccess(resultDto);
                } else {
                    resultDto.setDetailMessage("저장된 인슐린 정보가 없습니다.");
                    resultStatusService.setSuccess(resultDto);
                }

            } catch (Exception e) {
                log.error("Error while saving medication/insulin status", e);
                resultDto.setDetailMessage("저장 실패");
                resultStatusService.setFail(resultDto);
            }
        } else {
            resultDto.setDetailMessage("보호자 정보를 찾을 수 없습니다.");
            resultStatusService.setFail(resultDto);
        }

        return resultDto;
    }

    //식사 기록 조회 (전체)
    @Override
    public FoodAllDetailDto getFoodAllDetail(LocalDate date,HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);

        Optional<List<MealInfo>> mealInfoList = mealDao.getByUserAllMealInfo(date,user.get().getId());

        double totalProtein = 0.0;
        double totalCarbohydrate = 0.0;
        double totalFat = 0.0;


        for(MealInfo mealInfo : mealInfoList.get()){
            totalProtein += mealInfo.getFoodNutritionList().stream()
                    .mapToDouble(FoodNutrition::getProtein)
                    .sum();
            totalCarbohydrate += mealInfo.getFoodNutritionList().stream()
                    .mapToDouble(FoodNutrition::getCarbohydrate)
                    .sum();
            totalFat += mealInfo.getFoodNutritionList().stream()
                    .mapToDouble(FoodNutrition::getFat)
                    .sum();
        }

        return new FoodAllDetailDto(
                totalProtein,
                totalCarbohydrate,
                totalFat
        );
    }

    // 식사 시간에 따른 식사 기록 조회
    @Override
    public FoodAllDetailDto getFoodDetailByEatTime(LocalDate date,String eatTime, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        Optional<MealInfo> mealInfoOptional = mealDao.getMealInfoByEatTime(date,user.get().getId(),eatTime);
        log.info("[mealInfoOptional] {} : " + mealInfoOptional);

        if(mealInfoOptional.isPresent()){

        MealInfo mealInfo = mealInfoOptional.get();
        log.info("[mealInfo] : {}" + mealInfo);

        List<FoodNutrition> foodNutritionList = mealInfo.getFoodNutritionList();
        log.info("[foodNutritionList] : {}" + foodNutritionList);

        double protein = 0.0;
        double carbohydrate = 0.0;
        double totalFat  = 0.0;

        for(FoodNutrition foodNutrition : foodNutritionList){
             protein += foodNutrition.getProtein();
             carbohydrate+= foodNutrition.getCarbohydrate();
             totalFat += foodNutrition.getFat();
            }

            return new FoodAllDetailDto(
                    protein,
                    carbohydrate,
                    totalFat
            );
        }else {
            throw  new IllegalArgumentException("해당 식사 시간에 해당하는 정보가 없습니다.");

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