package com.springboot.peanut.service.Food.Impl;

import com.springboot.peanut.dto.food.FoodCheckDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.entity.FoodNutrition;
import com.springboot.peanut.entity.MealInfo;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.jwt.JwtProvider;
import com.springboot.peanut.repository.MealInfo.MealInfoRepository;
import com.springboot.peanut.service.Food.FoodCheckService;
import com.springboot.peanut.service.Jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodCheckServiceImpl implements FoodCheckService {
    private final JwtAuthenticationService jwtAuthenticationService;
    private final MealInfoRepository mealInfoRepository;

    @Override
    public FoodCheckDto getFoodCheckByDate(LocalDate date, HttpServletRequest request) {
        User user = jwtAuthenticationService.authenticationToken(request);
        double expectedBloodSugar = 0.0;
        String foodEatTime =null;
        List<String> foodName = null;
        Optional<List<MealInfo>> mealInfoList = mealInfoRepository.getByUserAllMealInfo(date,user.getId());
        for(MealInfo mealInfo:mealInfoList.get()){
            foodEatTime = mealInfo.getEatTime();
            foodName = mealInfo.getFoodNutritionList().stream()
                   .map(FoodNutrition::getName)
                    .collect(Collectors.toList());
            expectedBloodSugar = mealInfo.getExpectedBloodSugar();
        }
// 예상 혈당 상승량에 따른 피드백 설정
        String feedback = null;

        if (expectedBloodSugar <= 55) {
            feedback = "저혈당 지수 식품입니다. 혈당 변동이 크지 않을 것으로 예상됩니다.";
        } else if (expectedBloodSugar <= 69) {
            feedback = "중혈당 지수 식품입니다. 혈당 상승이 예상되니 주의하세요.";
        } else if (expectedBloodSugar >= 70) {
            feedback = "고혈당 지수 식품입니다. 혈당 급상승이 우려되니 섭취를 조절하세요.";
        } else {
            feedback = "혈당 지수에 따라 적절한 피드백을 제공할 수 없습니다.";
        }
        return new FoodCheckDto(
                foodName,
                foodEatTime,
                feedback
        );
    }
}
