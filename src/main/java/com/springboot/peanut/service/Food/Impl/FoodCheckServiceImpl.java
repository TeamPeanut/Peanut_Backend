package com.springboot.peanut.service.Food.Impl;

import com.springboot.peanut.data.dto.food.*;
import com.springboot.peanut.data.entity.BloodSugar;
import com.springboot.peanut.data.entity.FoodNutrition;
import com.springboot.peanut.data.entity.MealInfo;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.BloodSugar.BloodSugarRepository;
import com.springboot.peanut.data.repository.MealInfo.MealInfoRepository;
import com.springboot.peanut.service.Food.FoodCheckService;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.MainPage.GuardianMainPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodCheckServiceImpl implements FoodCheckService {
    private final JwtAuthenticationService jwtAuthenticationService;
    private final MealInfoRepository mealInfoRepository;
    private final BloodSugarRepository bloodSugarRepository;

    @Override
    public FoodCheckListDto getFoodCheckByDate(LocalDate date, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);

        // 전체 FoodCheckDto 리스트를 담을 리스트
        List<FoodCheckDto> foodCheckDtoList = new ArrayList<>();

        // 유저의 해당 날짜의 모든 식사 정보를 가져오기
        Optional<List<MealInfo>> mealInfoList = mealInfoRepository.getByUserAllMealInfo(date, user.get().getId());

        // 각 MealInfo에 대한 FoodCheckDto 생성
        for (MealInfo mealInfo : mealInfoList.orElse(new ArrayList<>())) {
            String foodEatTime = mealInfo.getEatTime();
            List<String> foodNames = mealInfo.getFoodNutritionList().stream()
                    .map(FoodNutrition::getName)
                    .collect(Collectors.toList());
            double expectedBloodSugar = mealInfo.getExpectedBloodSugar();

            // 예상 혈당 상승량에 따른 피드백 설정
            String feedback = getFeedbackByBloodSugar(expectedBloodSugar);
            String imageUrl = mealInfo.getImageUrl();

            // 새로운 FoodCheckDto 생성 후 리스트에 추가
            FoodCheckDto foodCheckDto = new FoodCheckDto(foodNames, foodEatTime, feedback, imageUrl);
            foodCheckDtoList.add(foodCheckDto);
        }

        // 모든 FoodCheckDto를 감싸는 FoodCheckListDto로 반환
        return new FoodCheckListDto(foodCheckDtoList);
    }




    // 식사 시간에 따른 식사 기록 조회
    @Override
    public FoodNameNutrionDto getFoodDetailByEatTime(LocalDate date, String eatTime, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        Optional<MealInfo> mealInfoOptional = mealInfoRepository.getMealInfoByEatTime(date, user.get().getId(), eatTime);
        log.info("[mealInfoOptional] {} : " + mealInfoOptional);

        if (mealInfoOptional.isPresent()) {
            MealInfo mealInfo = mealInfoOptional.get();
            log.info("[mealInfo] : {}" + mealInfo);

            List<FoodNutrition> foodNutritionList = mealInfo.getFoodNutritionList();
            log.info("[foodNutritionList] : {}" + foodNutritionList);

            // 음식 이름 리스트 생성
            List<String> foodNames = foodNutritionList.stream()
                    .map(FoodNutrition::getName)
                    .collect(Collectors.toList());

            // 영양 성분 계산
            double protein = 0.0;
            double carbohydrate = 0.0;
            double totalFat = 0.0;

            for (FoodNutrition foodNutrition : foodNutritionList) {
                protein += foodNutrition.getProtein();
                carbohydrate += foodNutrition.getCarbohydrate();
                totalFat += foodNutrition.getFat();
            }

            // FoodAllDetailDto에 영양 정보와 음식 이름 리스트 함께 반환
            return new FoodNameNutrionDto(protein, carbohydrate, totalFat, foodNames);
        } else {
            throw new IllegalArgumentException("해당 식사 시간에 해당하는 정보가 없습니다.");
        }
    }

    @Override
    public FoodBloodSugarDto getFoodFeedBackBloodSugarInfo(LocalDate date, String eatTime, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);

        if (!user.isPresent()) {
            throw new RuntimeException("사용자 인증에 실패했습니다.");
        }

        Optional<List<MealInfo>> mealInfoListOpt = mealInfoRepository.getMealInfoListByEatTime(date, user.get().getId(), eatTime);
        Optional<BloodSugar> closetBloodSugarOpt = bloodSugarRepository.findClosestBloodSugar(user.get().getId());
        log.info("[closetBloodSugar] : {}", closetBloodSugarOpt);

        if (!mealInfoListOpt.isPresent() || mealInfoListOpt.get().isEmpty()) {
            throw new IllegalArgumentException("해당 식사 시간에 대한 기록이 없습니다.");
        }

        List<MealInfo> mealInfoList = mealInfoListOpt.get();

        // 예상 혈당 수치의 총합과 평균을 계산하기 위한 변수들
        double totalExpectedBloodSugar = 0.0;
        int mealCount = mealInfoList.size();

        // 각 식사 기록에서 예상 혈당 수치를 추출
        for (MealInfo mealInfo : mealInfoList) {
            totalExpectedBloodSugar += mealInfo.getExpectedBloodSugar();
            log.info("[mealInfo expectedBloodSugar] : {}", mealInfo.getExpectedBloodSugar());
        }

        // 평균 예상 혈당 수치 계산
        double averageExpectedBloodSugar = totalExpectedBloodSugar;
        log.info("[averageExpectedBloodSugar] : {}", averageExpectedBloodSugar);

        // BloodSugar 정보도 반환할 경우, 최근 혈당 정보도 함께 반환
        double currentBloodSugarLevel = 0.0;
        if (closetBloodSugarOpt.isPresent()) {
            currentBloodSugarLevel = Double.parseDouble(closetBloodSugarOpt.get().getBloodSugarLevel());
        }
        String msg = getFeedbackByBloodSugar(averageExpectedBloodSugar, currentBloodSugarLevel);
        // FoodBloodSugarDto로 예상 혈당 수치와 최근 혈당 정보 반환
        FoodBloodSugarDto foodBloodSugarDto = new FoodBloodSugarDto(
                averageExpectedBloodSugar, // 평균 예상 혈당 수치
                currentBloodSugarLevel,
                msg
        );

        return foodBloodSugarDto;
    }

    // 예상 혈당 상승량에 따른 피드백 설정 메서드
    private String getFeedbackByBloodSugar(double expectedBloodSugar) {
        if (expectedBloodSugar <= 55) {
            return "저혈당 지수 식품입니다. 혈당 변동이 크지 않을 것으로 예상됩니다.";
        } else if (expectedBloodSugar <= 69) {
            return "중혈당 지수 식품입니다. 혈당 상승이 예상되니 주의하세요.";
        } else if (expectedBloodSugar >= 70) {
            return "고혈당 지수 식품입니다. 혈당 급상승이 우려되니 섭취를 조절하세요.";
        } else {
            return "혈당 지수에 따라 적절한 피드백을 제공할 수 없습니다.";
        }
    }

    private String getFeedbackByBloodSugar(double preMealBloodSugar, double averageExpectedBloodSugar) {
        // 혈당 차이 계산 (예상 혈당 상승량)
        double bloodSugarDifference = Math.abs(averageExpectedBloodSugar - preMealBloodSugar);

        // 피드백 메시지 설정
        if (bloodSugarDifference <= 10) {
            return String.format("혈당 차이는 %.1f입니다. 혈당 변동이 크지 않을 것으로 예상됩니다.", bloodSugarDifference);
        } else if (bloodSugarDifference <= 30) {
            return String.format("혈당 차이는 %.1f입니다. 혈당 상승이 예상되니 주의하세요.", bloodSugarDifference);
        } else {
            return String.format("혈당 차이는 %.1f입니다. 혈당 급상승이 우려되니 섭취를 조절하세요.", bloodSugarDifference);
        }
    }
}
