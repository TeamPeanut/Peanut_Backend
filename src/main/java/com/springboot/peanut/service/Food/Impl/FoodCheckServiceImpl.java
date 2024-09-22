package com.springboot.peanut.service.Food.Impl;

import com.springboot.peanut.data.dto.food.FoodCheckDto;
import com.springboot.peanut.data.dto.food.FoodCheckListDto;
import com.springboot.peanut.data.entity.FoodNutrition;
import com.springboot.peanut.data.entity.MealInfo;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.MealInfo.MealInfoRepository;
import com.springboot.peanut.service.Food.FoodCheckService;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodCheckServiceImpl implements FoodCheckService {
    private final JwtAuthenticationService jwtAuthenticationService;
    private final MealInfoRepository mealInfoRepository;

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
}
