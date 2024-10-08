package com.springboot.peanut.service.MainPage.Impl;

import com.springboot.peanut.data.dto.food.FoodAllDetailDto;
import com.springboot.peanut.data.dto.mainPage.GuardianMainPageGetAdditionalInfoDto;
import com.springboot.peanut.data.dto.mainPage.PatientMainPageGetAdditionalInfoDto;
import com.springboot.peanut.data.dto.mainPage.MainPageGetUserDto;
import com.springboot.peanut.data.entity.*;
import com.springboot.peanut.data.repository.BloodSugar.BloodSugarRepository;
import com.springboot.peanut.data.repository.Insulin.InsulinRepository;
import com.springboot.peanut.data.repository.MealInfo.MealInfoRepository;
import com.springboot.peanut.data.repository.Medicine.MedicineRepository;
import com.springboot.peanut.data.repository.PatientGuardianRepository;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.MainPage.GuardianMainService;
import com.springboot.peanut.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuardianMainServiceImpl implements GuardianMainService {
    private final BloodSugarRepository bloodSugarRepository;
    private final MedicineRepository medicineRepository;
    private final InsulinRepository insulinRepository;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final MealInfoRepository mealInfoRepository;
    private final NotificationService notificationService;
    private final PatientGuardianRepository patientGuardianRepository;

    @Override
    public MainPageGetUserDto getPatientUserInfoMainPage(HttpServletRequest request) {
        Optional<User> guardian = jwtAuthenticationService.authenticationToken(request);
        PatientGuardian patientGuardian = patientGuardianRepository.findByGuardianId(guardian.get().getId());
        User user = patientGuardian.getPatient();



        // 사용자 공복 혈당
        Optional<BloodSugar> fastingBloodSugar = bloodSugarRepository.findFastingBloodSugar(user.getId());
        String fastingBloodSugarLevel = fastingBloodSugar
                .map(BloodSugar::getBloodSugarLevel)
                .orElse("공복 혈당을 찾을 수 없습니다.");

        // 현재 시간과 가장 가까운 혈당
        Optional<BloodSugar> currentBloodSugarLevel = bloodSugarRepository.findClosestBloodSugar(user.getId());
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
    public GuardianMainPageGetAdditionalInfoDto getPatientAdditionalInfoMainPage(HttpServletRequest request, LocalDate date) {
        Optional<User> guardian = jwtAuthenticationService.authenticationToken(request);
        PatientGuardian patientGuardian = patientGuardianRepository.findByGuardianId(guardian.get().getId());
        User user = patientGuardian.getPatient();


        Optional<Medicine> medicine = medicineRepository.findByTodayMedicineInfo(user.getId(), date);
        Optional<Insulin> insulin = insulinRepository.findByTodayInsulinName(user.getId(),date);
        List<BloodSugar> bloodSugarList = bloodSugarRepository.findTodayBloodSugar(user.getId(),date);


        String medicineName = medicine.map(Medicine::getMedicineName).orElse("복용 기록 없음");
        Boolean medicineAlam = medicine.map(Medicine::isMedicationStatus).orElse(false);

        String insulinName = insulin.map(Insulin::getProductName).orElse("투여 기록 없음");
        Boolean insulinAlam = insulin.map(Insulin::isInsulinStatus).orElse(false);

        List<Map<String,Map<Integer, LocalDateTime>>> bloodSugarLevels = bloodSugarList.stream()
                .map(bloodSugar -> {
                    Map<Integer, LocalDateTime> innerMap = new HashMap<>();
                    innerMap.put(Integer.parseInt(bloodSugar.getBloodSugarLevel()), bloodSugar.getMeasurementTime());

                    Map<String,Map<Integer,LocalDateTime>> map = new HashMap<>();
                    map.put(bloodSugar.getMeasurementCondition(), innerMap);
                    return map;
                })
                .collect(Collectors.toList());

        return new GuardianMainPageGetAdditionalInfoDto(
                bloodSugarLevels,
                medicineName,
                medicineAlam,
                insulinName,
                insulinAlam
        );
    }

    //식사 기록 조회 (전체)
    @Override
    public FoodAllDetailDto getPatientFoodAllDetail(LocalDate date, HttpServletRequest request) {
        Optional<User> guardian = jwtAuthenticationService.authenticationToken(request);
        PatientGuardian patientGuardian = patientGuardianRepository.findByGuardianId(guardian.get().getId());
        User user = patientGuardian.getPatient();


        Optional<List<MealInfo>> mealInfoList = mealInfoRepository.getByUserAllMealInfo(date,user.getId());

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
    public FoodAllDetailDto getPatientFoodDetailByEatTime(LocalDate date,String eatTime, HttpServletRequest request) {
        Optional<User> guardian = jwtAuthenticationService.authenticationToken(request);
        PatientGuardian patientGuardian = patientGuardianRepository.findByGuardianId(guardian.get().getId());
        User user = patientGuardian.getPatient();


        Optional<MealInfo> mealInfoOptional = mealInfoRepository.getMealInfoByEatTime(date,user.getId(),eatTime);
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