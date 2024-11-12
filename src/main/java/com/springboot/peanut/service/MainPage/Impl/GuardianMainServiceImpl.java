package com.springboot.peanut.service.MainPage.Impl;

import com.springboot.peanut.data.dao.*;
import com.springboot.peanut.data.dto.food.FoodAllDetailDto;
import com.springboot.peanut.data.dto.mainPage.GuardianMainPageGetAdditionalInfoDto;
import com.springboot.peanut.data.dto.mainPage.MainPageGetUserDto;
import com.springboot.peanut.data.dto.mainPage.MedicineInsulinStatusRequestDto;
import com.springboot.peanut.data.dto.mainPage.PatientMainPageGetAdditionalInfoDto;
import com.springboot.peanut.data.dto.notification.NotificationRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.*;
import com.springboot.peanut.data.repository.BloodSugar.BloodSugarRepository;
import com.springboot.peanut.data.repository.Insulin.InsulinRepository;
import com.springboot.peanut.data.repository.Intake.IntakeRepository;
import com.springboot.peanut.data.repository.MealInfo.MealInfoRepository;
import com.springboot.peanut.data.repository.Medicine.MedicineRepository;
import com.springboot.peanut.data.repository.PatientGuardianRepository;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.MainPage.GuardianMainService;
import com.springboot.peanut.service.MainPage.MainPageTimeService;
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
public class GuardianMainServiceImpl implements GuardianMainService {

    private final BloodSugarDao bloodSugarDao;
    private final NotificationDao notificationDao;
    private final InsulinRecordDao insulinRecordDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final MedicineDao medicineDao;
    private final PatientGuardianRepository patientGuardianRepository;
    private final ResultStatusService resultStatusService;
    private final MedicineRecordDao medicineRecordDao;
    private final InsulinDao insulinDao;
    private  final MainPageTimeService mainPageTimeService;
    private final NotificationService notificationService;
    private final MealDao mealDao;

    @Override
    public MainPageGetUserDto getUserInfoMainPage(HttpServletRequest request) {
        Optional<User> guardian = jwtAuthenticationService.authenticationToken(request);
        PatientGuardian patientGuardian = patientGuardianRepository.findByGuardianId(guardian.get().getId());
        User user = patientGuardian.getPatient();
        // 사용자 공복 혈당
        Optional<BloodSugar> fastingBloodSugar = bloodSugarDao.findFastingBloodSugar(user.getId());
        String fastingBloodSugarLevel = fastingBloodSugar
                .map(BloodSugar::getBloodSugarLevel)
                .orElse("공복 혈당을 찾을 수 없습니다.");

        // 현재 시간과 가장 가까운 혈당
        Optional<BloodSugar> currentBloodSugarLevel = bloodSugarDao.findClosestBloodSugar(user.getId());
        String currentBloodSugar = currentBloodSugarLevel
                .map(BloodSugar::getBloodSugarLevel)
                .orElse("최근에 등록된 혈당을 찾을 수 없습니다.");

        // 생성자로 객체 생성
        return new MainPageGetUserDto(
                user.getId(),
                user.getUserName(),
                user.getProfileUrl(),
                fastingBloodSugarLevel,
                currentBloodSugar
        );
    }

    @Override
    public PatientMainPageGetAdditionalInfoDto getAdditionalInfoMainPage(HttpServletRequest request, LocalDate date) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);

        // 혈당 정보 가져오기
        List<BloodSugar> bloodSugarList = Optional.ofNullable(bloodSugarDao.findTodayBloodSugar(user.get().getId(), date))
                .orElse(Collections.emptyList());

        // 약 정보 가져오기
        List<Medicine> medicineList = Optional.ofNullable(mainPageTimeService.getMedicineListByTime(user.get().getId()))
                .orElse(Collections.emptyList());

        // 약 복용 기록 정보 가져오기
        Optional<MedicineRecord> medicineRecordInfo = mainPageTimeService.getMedicineRecordByTime(user.get().getId(), date);
        boolean medicineStatus = medicineRecordInfo.map(MedicineRecord::isMedicineStatus).orElse(false);

        // 인슐린 정보 가져오기
        Optional<Insulin> insulin = Optional.ofNullable(insulinDao.getInsulinByUserId(user.get().getId()));
        Optional<InsulinRecord> insulinRecord = mainPageTimeService.getInsulinRecordTime(user.get().getId(), date);
        boolean insulinStatus = insulinRecord.map(InsulinRecord::isInsulinStatus).orElse(false);

        // 약과 인슐린 관련 정보 설정
        Medicine medicine = medicineList.isEmpty() ? null : medicineList.get(0);
        String medicineName = (medicine != null) ? medicine.getMedicineName() : "약 정보 없음";

        List<String> intakeTimes = medicine != null ? medicine.getIntakes().stream()
                .flatMap(intake -> Optional.ofNullable(intake.getIntakeTime()).orElse(Collections.emptyList()).stream())
                .collect(Collectors.toList()) : Collections.emptyList();
        String medicineTime = mainPageTimeService.getIntakeTimeByCurrentTime(intakeTimes);

        String insulinName = insulin.map(Insulin::getProductName).orElse("인슐린 정보 없음");
        List<String> insulinTimeList = insulin.map(Insulin::getAdministrationTime).orElse(Collections.emptyList());
        String insulinTime = mainPageTimeService.getInsulinTimeByCurrentTime(insulinTimeList);
        String insulinDosage = insulin.map(Insulin::getDosage).orElse("용량 정보 없음");

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
        Optional<User> guardian = jwtAuthenticationService.authenticationToken(request);
        PatientGuardian patientGuardian = patientGuardianRepository.findByGuardianId(guardian.get().getId());
        User user = patientGuardian.getPatient();

        ResultDto resultDto = new ResultDto();
        Long userId = user.getId();

        // Medicine과 Insulin 정보를 가져온다
        List<Medicine> medicineList = mainPageTimeService.getMedicineListByTime(userId);
        Insulin insulin = insulinDao.getInsulinByUserId(userId);

        boolean newMedicineStatus = medicineInsulinStatusRequestDto.isMedicineStatus();
        boolean newInsulinStatus = medicineInsulinStatusRequestDto.isInsulinStatus();

        try {
            // Medicine 상태 업데이트
            if (medicineList != null || medicineList.isEmpty()) {
                for (Medicine medicine : medicineList) {
                    // 기존에 같은 날짜에 기록된 MedicineRecord가 있는지 확인
                    Optional<List<MedicineRecord>> existingRecord = medicineRecordDao.findMedicineRecordByUserId(userId, date);
                    log.info("[existingRecord] : {} ",existingRecord.isPresent());
                    // 기존 기록이 존재하고 리스트가 비어있지 않은지 확인
                    if (existingRecord.isPresent() && !existingRecord.get().isEmpty()) {
                        // 기존 레코드가 있으면 상태만 업데이트
                        MedicineRecord medicineRecord = existingRecord.get().get(0);  // 첫 번째 레코드만 업데이트
                        medicineRecord.setGuardianMedicineStatus(newMedicineStatus);  // 상태만 업데이트
                        if(medicineRecord.isMedicineStatus()){
                            String fcmToken = user.getFcmToken();
                            String title = "보호자 알림";
                            String body = user.getUserName()+"님의 보호자께서 알림을 보냈습니다 \n 복약 시간이 지났습니다. 복약 후 복약 체크를 진행해주세요.";
                            notificationService.sendNotification(fcmToken,title,body);
                            log.info("[medicine] : {} 기존 레코드 상태 업데이트 완료", medicine.getMedicineName());
                        }
                    } else {
                        // 기존 레코드가 없으면 새 레코드 생성
                        MedicineRecord newMedicineRecord = MedicineRecord.createInsulinRecord(
                                date,
                                newMedicineStatus,
                                user,
                                medicine
                        );
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
                // 기존에 같은 날짜에 기록된 InsulinRecord가 있는지 확인
                Optional<InsulinRecord> existingInsulinRecord = insulinRecordDao.findInsulinRecordByUserId(userId, date);

                if (existingInsulinRecord.isPresent()) {
                    // 기존 레코드가 있으면 상태만 업데이트
                    InsulinRecord insulinRecord = existingInsulinRecord.get();
                    insulinRecord.setGuardianStatus(newInsulinStatus);  // 상태만 업데이트
                    if(insulinRecord.isGuardianStatus()){
                        String fcmToken = user.getFcmToken();
                        String title = "보호자 알림";
                        String body = user.getUserName()+"님의 보호자께서 알림을 보냈습니다 \n 인슐린 투약 시간이 지났습니다. 투약 후 복약 체크를 진행해주세요.";

                        notificationService.sendNotification(fcmToken,title,body);
                        log.info("[fcmToken] : {}", fcmToken);
                        log.info("[userName] : {}", user.getUserName());
                        log.info("[body] : {}", body);
                        log.info("[insulin] : {} 기존 레코드 상태 업데이트 완료", insulin.getProductName());
                        saveNotification( title, body, fcmToken, user);
                    }
                } else {
                    // 기존 레코드가 없으면 새 레코드 생성
                    InsulinRecord newInsulinRecord = InsulinRecord.createInsulinRecord(
                            date,
                            newInsulinStatus,
                            user,
                            insulin
                    );
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

        return resultDto;
    }

    //식사 기록 조회 (전체)
    @Override
    public FoodAllDetailDto getFoodAllDetail(LocalDate date,HttpServletRequest request) {
        Optional<User> guardian = jwtAuthenticationService.authenticationToken(request);
        PatientGuardian patientGuardian = patientGuardianRepository.findByGuardianId(guardian.get().getId());
        User user = patientGuardian.getPatient();

        Optional<List<MealInfo>> mealInfoList = mealDao.getByUserAllMealInfo(date,user.getId());

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