package com.springboot.peanut.service.MainPage.Impl;

import com.springboot.peanut.data.dao.BloodSugarDao;
import com.springboot.peanut.data.dao.InsulinDao;
import com.springboot.peanut.data.dao.MealDao;
import com.springboot.peanut.data.dao.MedicineDao;
import com.springboot.peanut.data.dto.food.FoodAllDetailDto;
import com.springboot.peanut.data.dto.mainPage.MedicineInsulinStatusRequestDto;
import com.springboot.peanut.data.dto.mainPage.PatientMainPageGetAdditionalInfoDto;
import com.springboot.peanut.data.dto.mainPage.MainPageGetUserDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.*;
import com.springboot.peanut.data.repository.BloodSugar.BloodSugarRepository;
import com.springboot.peanut.data.repository.Insulin.InsulinRepository;
import com.springboot.peanut.data.repository.Intake.IntakeRepository;
import com.springboot.peanut.data.repository.MealInfo.MealInfoRepository;
import com.springboot.peanut.data.repository.Medicine.MedicineRepository;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientMainPageServiceImpl implements PatientMainPageService {

    private final BloodSugarDao bloodSugarDao;
    private final IntakeRepository intakeRepository;
    private final InsulinRepository insulinRepository;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final MealDao mealDao;
    private final NotificationService notificationService;
    private final ResultStatusService resultStatusService;
    private final MedicineDao medicineDao;
    private final InsulinDao insulinDao;
    private  final MainPageTimeService mainPageTimeService;

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
    public PatientMainPageGetAdditionalInfoDto getAdditionalInfoMainPage(HttpServletRequest request,LocalDate date) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        Optional<Intake> intake = intakeRepository.findByTodayIntakeStatus(user.get().getId(), date);
        List<BloodSugar> bloodSugarList = bloodSugarDao.findTodayBloodSugar(user.get().getId(),date);
        List<Medicine> medicineList = mainPageTimeService.getMedicineListByTime(user.get().getId());
        Insulin insulin = mainPageTimeService.getInsulinyTime(user.get().getId());

        String medicineName = medicineList.get(0).getMedicineName();
        String medicineTime = intake.map(i ->
                i.getIntakeTime().isEmpty() ? "오늘 복용 없음" : i.getIntakeTime().get(0)
        ).orElse("투여 기록 없음");
        boolean medicineStatus = medicineList.get(0).isMedicationStatus();

        String insulinName = insulin.getProductName();
        boolean insulinStatus = insulin.isInsulinStatus();
        List<String> insulinTimeList = insulin.getAdministrationTime();
        String insulinTime = mainPageTimeService.getInsulinTimeByCurrentTime(insulinTimeList);
        // 상태를 필요에 따라 초기화
        if (medicineStatus) {
            medicineStatus = mainPageTimeService.getStatus(medicineStatus);
            log.info("[medicineStatus] : {}", medicineStatus);
        }
        if (insulinStatus) {
            insulinStatus = mainPageTimeService.getStatus(medicineStatus);
            log.info("[insulinStatus] : {}", insulinStatus);
        }

         List<Map<String,Map<Integer, LocalDateTime>>> bloodSugarLevels = bloodSugarList.stream()
                .map(bloodSugar -> {
                    Map<Integer, LocalDateTime> innerMap = new HashMap<>();
                    innerMap.put(Integer.parseInt(bloodSugar.getBloodSugarLevel()), bloodSugar.getMeasurementTime());

                    Map<String,Map<Integer,LocalDateTime>> map = new HashMap<>();
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
                insulinTime
        );
    }


    @Override
    @Transactional
    public ResultDto saveMedicineInsulinStatus(HttpServletRequest request, LocalDate date, MedicineInsulinStatusRequestDto medicineInsulinStatusRequestDto) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        ResultDto resultDto = new ResultDto();
        Long userId = user.get().getId();

        // Medicine과 Insulin 정보를 가져온다
        List<Medicine> medicineList = mainPageTimeService.getMedicineListByTime(user.get().getId());
        Insulin insulin = mainPageTimeService.getInsulinyTime(userId);

        boolean newMedicineStatus = medicineInsulinStatusRequestDto.isMedicineStatus();
        boolean newInsulinStatus = medicineInsulinStatusRequestDto.isInsulinStatus();

        try {
            // Medicine 상태 업데이트
            if(medicineList != null && !medicineList.isEmpty()) {
                for(Medicine medicine : medicineList) {
                    medicine.updateMedicationStatus(newMedicineStatus);
                    medicineDao.saveMedicineInfo(medicine);
                    log.info("[medicine] : {}" ,medicine);
                    resultDto.setDetailMessage("약 상태 저장 완료");
                    resultStatusService.setSuccess(resultDto);
                }
            }else {
                resultDto.setDetailMessage("저장된 복용 정보가 없습니다.");
                resultStatusService.setSuccess(resultDto);
            }

            if(insulin != null) {
                    insulin.updateInsulinStatus(newInsulinStatus);
                    insulinDao.saveInsulin(insulin);
                    log.info("[medicine] : {}" ,insulin);
                    resultDto.setDetailMessage("약 상태 저장 완료");
                    resultStatusService.setSuccess(resultDto);

            }else {
                resultDto.setDetailMessage("저장된 복용 정보가 없습니다.");
                resultStatusService.setSuccess(resultDto);
            }

            resultDto.setDetailMessage("저장 완료");
            resultStatusService.setSuccess(resultDto);
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


}