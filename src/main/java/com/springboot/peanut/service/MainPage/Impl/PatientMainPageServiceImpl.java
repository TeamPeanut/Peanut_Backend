package com.springboot.peanut.service.MainPage.Impl;

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
import com.springboot.peanut.service.MainPage.PatientMainPageService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientMainPageServiceImpl implements PatientMainPageService {

    private final BloodSugarRepository bloodSugarRepository;
    private final MedicineRepository medicineRepository;
    private final IntakeRepository intakeRepository;
    private final InsulinRepository insulinRepository;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final MealInfoRepository mealInfoRepository;
    private final NotificationService notificationService;
    private final ResultStatusService resultStatusService;

    @Override
    public MainPageGetUserDto getUserInfoMainPage(HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        // 사용자 공복 혈당
        Optional<BloodSugar> fastingBloodSugar = bloodSugarRepository.findFastingBloodSugar(user.get().getId());
        String fastingBloodSugarLevel = fastingBloodSugar
                .map(BloodSugar::getBloodSugarLevel)
                .orElse("공복 혈당을 찾을 수 없습니다.");

        // 현재 시간과 가장 가까운 혈당
        Optional<BloodSugar> currentBloodSugarLevel = bloodSugarRepository.findClosestBloodSugar(user.get().getId());
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

        Optional<Medicine> medicine = medicineRepository.findMedicationInfoAndMedicationTimeForUser(user.get().getId(), date);
        Optional<Intake> intake = intakeRepository.findByTodayIntakeStatus(user.get().getId(), date);
        Optional<Insulin> insulin = insulinRepository.findInsulinInfoByDate(user.get().getId(),date);
        List<BloodSugar> bloodSugarList = bloodSugarRepository.findTodayBloodSugar(user.get().getId(),date);


        String medicineName = medicine.map(Medicine::getMedicineName).orElse("복용 기록 없음");
        String medicineTime = intake.map(i ->
                i.getIntakeTime().isEmpty() ? "오늘 복용 없음" : i.getIntakeTime().get(0)
        ).orElse("투여 기록 없음");
        boolean medicineStatus = medicine.map(Medicine::isMedicationStatus).orElse(false);

        String insulinName = insulin.map(Insulin::getProductName).orElse("투여 기록 없음");
        boolean insulinStatus = insulin.map(Insulin::isInsulinStatus).orElse(false);

        getMedicineStatus(medicineStatus);
        getInsulinStatus(insulinStatus);

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
                insulinStatus
        );
    }


    @Override
    public ResultDto saveMedicineInsulinStatus(HttpServletRequest request, LocalDate date, MedicineInsulinStatusRequestDto medicineInsulinStatusRequestDto) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        ResultDto resultDto = new ResultDto();
        // User ID를 가져온다
        Long userId = user.get().getId();

        // Medicine과 Insulin 정보를 가져온다
        Optional<Medicine> medicine = medicineRepository.findMedicationInfoAndMedicationTimeForUser(userId, date);
        Optional<Insulin> insulin = insulinRepository.findInsulinInfoByDate(userId, date);

        // 요청에서 받은 상태 정보를 가져온다
        boolean newMedicineStatus = medicineInsulinStatusRequestDto.isMedicineStatus();
        boolean newInsulinStatus = medicineInsulinStatusRequestDto.isInsulinStatus();

        // Medicine 상태 업데이트
        if (medicine.isPresent()) {
            Medicine med = medicine.get();
            med.setMedicationStatus(newMedicineStatus); // 새 상태로 업데이트
            medicineRepository.save(med); // 변경사항을 DB에 저장
        }

        // Insulin 상태 업데이트
        if (insulin.isPresent()) {
            Insulin ins = insulin.get();
            ins.setInsulinStatus(newInsulinStatus); // 새 상태로 업데이트
            insulinRepository.save(ins);
        }

        // 결과 메시지 설정
        resultDto.setDetailMessage("저장 완료");
        resultStatusService.setSuccess(resultDto);

        // 결과 DTO를 반환
        return resultDto; // 적절한 결과 메시지 반환
    }


    //식사 기록 조회 (전체)
    @Override
    public FoodAllDetailDto getFoodAllDetail(LocalDate date,HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);

        Optional<List<MealInfo>> mealInfoList = mealInfoRepository.getByUserAllMealInfo(date,user.get().getId());

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
        Optional<MealInfo> mealInfoOptional = mealInfoRepository.getMealInfoByEatTime(date,user.get().getId(),eatTime);
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

    public boolean getMedicineStatus(boolean medicineStatus) {
        // 현재 시간 확인
        LocalTime currentTime = LocalTime.now();

        // 아침, 점심, 저녁 시간대에 따라 상태 초기화
        if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(11, 0))) {
            // 아침 시간대
            medicineStatus = false;
        } else if (currentTime.isAfter(LocalTime.of(11, 0)) && currentTime.isBefore(LocalTime.of(17, 0))) {
            // 점심 시간대
            medicineStatus = false; // 점심 시간에는 초기화
        } else if (currentTime.isAfter(LocalTime.of(17, 0)) && currentTime.isBefore(LocalTime.of(22, 0))) {
            // 저녁 시간대
            medicineStatus = false; // 저녁 시간에는 초기화
        } else {
            // 자기 전 시간대
            medicineStatus = false; // 자기 전 시간에는 초기화
        }

        return medicineStatus;
    }

    private boolean getInsulinStatus(boolean insulinStatus) {
        // 현재 시간 확인
        LocalTime currentTime = LocalTime.now();

        // 아침, 점심, 저녁 시간대에 따라 상태 초기화
        if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(11, 0))) {
            // 아침 시간대
            insulinStatus =false;
        } else if (currentTime.isAfter(LocalTime.of(11, 0)) && currentTime.isBefore(LocalTime.of(17, 0))) {
            // 점심 시간대
            insulinStatus = false; // 점심 시간에는 초기화
        } else if (currentTime.isAfter(LocalTime.of(17, 0)) && currentTime.isBefore(LocalTime.of(22, 0))) {
            // 저녁 시간대
            insulinStatus = false; // 저녁 시간에는 초기화
        } else {
            // 자기 전 시간대
            insulinStatus = false; // 자기 전 시간에는 초기화
        }

        return insulinStatus;
    }

}