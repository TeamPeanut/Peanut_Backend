package com.springboot.peanut.service.Food.Impl;

import com.springboot.peanut.dao.MealDao;
import com.springboot.peanut.dto.food.FoodNutritionDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.entity.BloodSugar;
import com.springboot.peanut.entity.FoodNutrition;
import com.springboot.peanut.entity.MealInfo;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.repository.BloodSugar.BloodSugarRepository;
import com.springboot.peanut.repository.FoodNutrition.FoodNutritionRepository;
import com.springboot.peanut.service.Food.FoodRecordNormalService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.Jwt.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodRecordNormalServiceImpl implements FoodRecordNormalService {

    private final FoodNutritionRepository foodNutritionRepository;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final BloodSugarRepository bloodSugarRepository;
    private final ResultStatusService resultStatusService;
    private final MealDao mealDao;

    @Override
    public List<FoodNutritionDto> getFoodNutritionByName(List<String> name, HttpServletRequest request) {
        User user = jwtAuthenticationService.authenticationToken(request);
        log.info("[user] : {} " , user.getEmail());
        List<FoodNutrition> foodNutritionList = foodNutritionRepository.findFoodNutritionByFoodNameKor(name);
        log.info("[foodNutritionList] : {} ", foodNutritionList );
        List<FoodNutritionDto> foodDetailInfoDtoList = foodNutritionList.stream().map(foodNutrition ->
                new FoodNutritionDto(
                        foodNutrition.getId(),
                        foodNutrition.getName(),
                        foodNutrition.getCarbohydrate(),
                        foodNutrition.getProtein(),
                        foodNutrition.getFat(),
                        foodNutrition.getCholesterol(),
                        foodNutrition.getGlIndex(),
                        foodNutrition.getGiIndex()
                )).collect(Collectors.toList());

        request.getSession().setAttribute("foodDetailInfoDtoList", foodDetailInfoDtoList);
        return foodDetailInfoDtoList;

    }

    @Override
    public ResultDto saveNormalMealInfo(String mealTime, int servingCount, HttpServletRequest request) {
      User user = jwtAuthenticationService.authenticationToken(request);
      List<FoodNutritionDto> foodNutritionDtoList = (List<FoodNutritionDto>)request.getSession().getAttribute("foodDetailInfoDtoList");

      if(user == null){
          ResultDto resultDto = new ResultDto();
          resultDto.setDetailMessage("사용자 인증에 실패했습니다.");
          resultStatusService.setFail(resultDto);  // 실패 응답 설정
          return resultDto;
      }

      List<Long> foodNutritionIds = foodNutritionDtoList.stream()
              .map(FoodNutritionDto::getFoodId)
              .collect(Collectors.toList());


      List<FoodNutrition> foodNutritionList = foodNutritionRepository.findAllById(foodNutritionIds);

      double expectedBloodsugar = calculateExpectedBloodSugar(user.getId(), servingCount,foodNutritionList);
      MealInfo mealInfo = MealInfo.MealInfo(mealTime,expectedBloodsugar,foodNutritionList,user);

      mealDao.save(mealInfo);

      ResultDto resultDto = new ResultDto();
      resultDto.setDetailMessage("식사 기록 저장 완료!");
      resultStatusService.setSuccess(resultDto);

      return resultDto;
    }

    public double calculateExpectedBloodSugar(Long userId, int servingCount, List<FoodNutrition> foodNutritionList) {
        // 사용자 아이디로 최근 혈당 기록 가져오기
        Optional<BloodSugar> currentBloodSugar = bloodSugarRepository.findClosestBloodSugar(userId);
        log.info("[currentBloodSugar] : {} ", currentBloodSugar );
        if (!currentBloodSugar.isPresent()) {
            throw new RuntimeException("최근 등록된 혈당 기록이 없습니다.");
        }

        // 현재 혈당 값을 가져옴
        double currentBloodSugarinfo = Double.parseDouble(currentBloodSugar.get().getBloodSugarLevel());
        double totalBloodSugarIncrease = 0.0;

        // 음식의 GI, GL, 탄수화물 정보를 기반으로 혈당 상승량 계산
        for (FoodNutrition food : foodNutritionList) {
            // GL 계산
            double gl = (food.getGiIndex() * food.getCarbohydrate()) / 100.0;

            // 예상 혈당 상승량 계산 (GL * 1.8)
            double bloodSugarIncrease = gl * 1.8;

            // 총 혈당 상승량에 더하기
            totalBloodSugarIncrease += bloodSugarIncrease;
        }
        // 평균 혈당 상승량 계산
        double averageBloodSugarIncrease = totalBloodSugarIncrease / foodNutritionList.size();

        // 최종 예상 혈당 계산 (현재 혈당 + 평균 혈당 상승량)
        double expectedBloodSugar = (currentBloodSugarinfo + averageBloodSugarIncrease)*servingCount;

        return expectedBloodSugar;
    }
}
